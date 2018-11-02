package cn.com.bigknow.trade.pos.Immediate.app.provider.request;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.apkfuns.logutils.LogUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hujie on 16/6/18.
 */
public class RequestInterceptor implements Interceptor {

    String appVersion = "1.0";


    public RequestInterceptor(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    context.getPackageName(), 0);
            appVersion = info.versionName;
        } catch (Exception e) {

        }
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();
        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("softVersion", appVersion)
                .addQueryParameter("deviceType", "android")

                .build();
        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder()
                .url(url);

        String token = cn.com.bigknow.trade.pos.Immediate.app.util.UserManager.getInstance().getToken();

        if (token != null && !token.isEmpty()) {
            requestBuilder.addHeader("BIGKNOW_USER_TOKEN", token);
        }

        Request request = requestBuilder.build();

        LogUtils.d("请求地址为:--->"+url);

        return chain.proceed(request);
    }
}
