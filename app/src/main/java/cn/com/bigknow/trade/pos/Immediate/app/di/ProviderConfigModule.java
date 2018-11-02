package cn.com.bigknow.trade.pos.Immediate.app.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import cn.com.bigknow.trade.pos.Immediate.app.provider.request.FastJsonCoverterFactory;
import cn.com.bigknow.trade.pos.Immediate.app.provider.request.RequestInterceptor;
import cn.com.bigknow.trade.pos.Immediate.base.provider.EventBusConfig;
import cn.com.bigknow.trade.pos.Immediate.base.provider.EventBusIndex;
import cn.com.bigknow.trade.pos.Immediate.base.provider.HttpClientConfig;
import cn.com.bigknow.trade.pos.Immediate.base.provider.RetrofitConfig;
import cn.com.bigknow.trade.pos.Immediate.base.provider.cookie.OkHttpCookieJar;
import cn.com.bigknow.trade.pos.Immediate.model.ModelConfig;
import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import okhttp3.Interceptor;

/**
 * Created by hujie on 16/6/18.
 */
@Module
public class ProviderConfigModule {


    @Provides
    @Singleton
    Interceptor providerInterceptor(Context context) {
        return new RequestInterceptor(context);
    }

    @Singleton
    @Provides
    CookieJar providerCoolkieJar(Application application) {
        return new OkHttpCookieJar(application);
    }


    @Singleton
    @Provides
    HttpClientConfig provideHttpClientConfig(CookieJar cookieJar, Interceptor interceptor) {
        HttpClientConfig config = new HttpClientConfig();
        config.setConnectTimeout(3 * 60);
        config.setReadTimeout(3 * 60);
        config.setWriteTimeout(5 * 60);
        config.setRetryOnConnectionFailure(true);
        config.setCookieJar(cookieJar);
        config.setInterceptor(interceptor);

        return config;
    }

    @Singleton
    @Provides
    RetrofitConfig provideRestConfig() {
        RetrofitConfig config = new RetrofitConfig();
        config.setBaseUrl(ModelConfig.MVP_URL);
        config.setJsonFactory(FastJsonCoverterFactory.create());
        return config;
    }

    @Singleton
    @Provides
    EventBusConfig provideEventBusConfig() {
        EventBusConfig config = new EventBusConfig();
        config.setInfoIndex(new EventBusIndex());
        return config;
    }


}
