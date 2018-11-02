package cn.com.bigknow.trade.pos.Immediate.base.provider.cookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class PersistentHttpCookieStore {
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private static final String COOKIE_NAME_PREFIX = "cookie_";

    private final HashMap<String, ConcurrentHashMap<String, Cookie>> cookies;
    private final SharedPreferences cookiePrefs;

    /**
     * Construct a persistent cookie store.
     *
     * @param context
     *            Context to attach cookie store to
     */
    public PersistentHttpCookieStore(Context context) {
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        cookies = new HashMap<>();

        // Load any previously stored cookies into the store
        Map<String, ?> prefsMap = cookiePrefs.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            if (entry.getValue() != null
                    && !((String) entry.getValue())
                    .startsWith(COOKIE_NAME_PREFIX)) {
                String[] cookieNames = TextUtils.split(
                        (String) entry.getValue(), ",");
                for (String name : cookieNames) {
                    String encodedCookie = cookiePrefs.getString(
                            COOKIE_NAME_PREFIX + name, null);
                    if (encodedCookie != null) {
                        Cookie decodedCookie = decodeCookie(encodedCookie);
                        if (decodedCookie != null) {
                            if (!cookies.containsKey(getKey(decodedCookie))) {
                                cookies.put(getKey(decodedCookie), new ConcurrentHashMap<String, Cookie>());
                            }
                            cookies.get(getKey(decodedCookie)).put(name, decodedCookie);
                        }
                    }
                }

            }
        }
    }

    private String getKey(Cookie cookie) {
        return cookie.domain();
    }


    private boolean isCookieExpired(Cookie cookie) {
        if (!cookie.persistent()) {
            return false;
        } else {
            return cookie.expiresAt() < System.currentTimeMillis();
        }
    }

    public void add(HttpUrl uri, Cookie cookie) {
        String name = getCookieToken(cookie);


        // Save cookie into local store, or remove if expired
        if (!isCookieExpired(cookie)) {
            if (!cookies.containsKey(getKey(cookie))) {
                cookies.put(getKey(cookie), new ConcurrentHashMap<String, Cookie>());
            }
            cookies.get(getKey(cookie)).put(name, cookie);
        } else {
            if (cookies.containsKey(getKey(cookie))) {
                cookies.get(getKey(cookie)).remove(name);
            } else {
                return;
            }
        }

        // Save cookie into persistent store
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.putString(getKey(cookie),
                TextUtils.join(",", cookies.get(getKey(cookie)).keySet()));
        prefsWriter.putString(COOKIE_NAME_PREFIX + name,
                encodeCookie(new HttpCookieParcelable(cookie)));
        prefsWriter.apply();
    }

    protected String getCookieToken(Cookie cookie) {
        return cookie.name()+"@" + cookie.domain();
    }


    public List<Cookie> get(HttpUrl uri) {


        String domian = uri.host().substring(uri.host().indexOf(".") + 1);

        ArrayList<Cookie> ret = new ArrayList<>();
        if (cookies.containsKey(domian)) {
            ret.addAll(cookies.get(domian).values());
        }
        return ret;
    }

    public boolean removeAll() {
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.apply();
        cookies.clear();
        return true;
    }

    public boolean remove(HttpUrl uri, Cookie cookie) {
        String name = getCookieToken(cookie);

        if (cookies.containsKey(getKey(cookie))
                && cookies.get(getKey(cookie)).containsKey(name)) {
            cookies.get(getKey(cookie)).remove(name);

            SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
            if (cookiePrefs.contains(COOKIE_NAME_PREFIX + name)) {
                prefsWriter.remove(COOKIE_NAME_PREFIX + name);
            }
            prefsWriter.putString(getKey(cookie),
                    TextUtils.join(",", cookies.get(getKey(cookie)).keySet()));
            prefsWriter.apply();

            return true;
        } else {
            return false;
        }
    }

    public List<Cookie> getCookies() {
        ArrayList<Cookie> ret = new ArrayList<>();
        for (String key : cookies.keySet()) {
            ret.addAll(cookies.get(key).values());
        }

        return ret;
    }

    public List<URI> getURIs() {
        ArrayList<URI> ret = new ArrayList<>();
        for (String key : cookies.keySet())
            try {
                ret.add(new URI(key));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        return ret;
    }

    /**
     * Serializes Cookie object into String
     *
     * @param cookie
     *            cookie to be encoded, can be null
     * @return cookie encoded as String
     */
    protected String encodeCookie(HttpCookieParcelable cookie) {
        if (cookie == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (IOException e) {

            return null;
        }

        return byteArrayToHexString(os.toByteArray());

    }

    /**
     * Returns cookie decoded from cookie string
     *
     * @param cookieString
     *            string of cookie as returned from http request
     * @return decoded cookie or null if exception occured
     */
    protected Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((HttpCookieParcelable) objectInputStream.readObject()).getCookies();
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }
        return cookie;
    }


    /**
     * Using some super basic byte array &lt;-&gt; hex conversions so we don't
     * have to rely on any large Base64 libraries. Can be overridden if you
     * like!
     *
     * @param bytes
     *            byte array to be converted
     * @return string containing hex values
     */
    protected String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /**
     * Converts hex values from strings to byte array
     *
     * @param hexString
     *            string of hex-encoded values
     * @return decoded byte array
     */
    protected byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
