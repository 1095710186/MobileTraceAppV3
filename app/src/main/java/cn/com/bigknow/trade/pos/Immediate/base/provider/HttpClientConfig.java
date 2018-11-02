package cn.com.bigknow.trade.pos.Immediate.base.provider;

import okhttp3.CookieJar;
import okhttp3.Interceptor;

/**
 * Created by hujie on 16/6/18.
 */
public class HttpClientConfig {

    private int connectTimeout;
    private int readTimeout;
    private int writeTimeout;
    private boolean retryOnConnectionFailure;
    private CookieJar cookieJar;
    private Interceptor interceptor;


    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public void setRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
        this.retryOnConnectionFailure = retryOnConnectionFailure;
    }

    public void setCookieJar(CookieJar cookieJar) {
        this.cookieJar = cookieJar;
    }

    public void setInterceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    public Interceptor getInterceptor() {
        return interceptor;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

}
