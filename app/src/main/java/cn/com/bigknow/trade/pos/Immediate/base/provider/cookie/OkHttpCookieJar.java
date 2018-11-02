package cn.com.bigknow.trade.pos.Immediate.base.provider.cookie;


import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by hujie on 16/5/30.
 */
public class OkHttpCookieJar implements CookieJar {

    PersistentHttpCookieStore httpCookieStore;


    public OkHttpCookieJar(Context context) {
        httpCookieStore = new PersistentHttpCookieStore(context);
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                httpCookieStore.add(url, item);
            }

        }

    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = httpCookieStore.get(url);
        return cookies;
    }
}
