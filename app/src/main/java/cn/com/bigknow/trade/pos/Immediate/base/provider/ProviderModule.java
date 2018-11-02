package cn.com.bigknow.trade.pos.Immediate.base.provider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by hujie on 16/6/18.
 */

@Module
public class ProviderModule {


    @Provides
    @Singleton
    public EventBus providerEventBus(EventBusConfig config) {
        EventBusBuilder builder = EventBus.builder()
                .logNoSubscriberMessages(config.isLogNoSubscriberMessages())
                .sendNoSubscriberEvent(config.isSendNoSubscriberEvent())
                .ignoreGeneratedIndex(config.isIgnoreGeneratedIndex())
                .throwSubscriberException(config.isThrowSubscriberException());
        if (config.getInfoIndex() != null) {
            builder.addIndex(config.getInfoIndex());
        }
        return builder.installDefaultEventBus();
    }


    @Singleton
    @Provides
    Retrofit provideRetrofit(final RetrofitConfig config, final OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(config.getBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(config.getJsonFactory())
                .addCallAdapterFactory(
                        RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    @Singleton
    @Provides
    OkHttpClient provideHttpClient(final HttpClientConfig config) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.writeTimeout(config.getReadTimeout(), TimeUnit.SECONDS)
                .connectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(config.getReadTimeout(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(config.isRetryOnConnectionFailure())
                .cookieJar(config.getCookieJar())
                .addInterceptor(config.getInterceptor());
        return builder.build();
    }


}
