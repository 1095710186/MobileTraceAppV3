package cn.com.bigknow.trade.pos.Immediate.app.di;

import cn.com.bigknow.trade.pos.Immediate.model.api.RetrofitApi;
import cn.com.bigknow.trade.pos.Immediate.model.errors.RxNetErrorProcessor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by hujie on 16/6/18.
 */
@Module
public class ApiModule {

    @Singleton
    @Provides
    RetrofitApi providerRetrofitApi(Retrofit retrofit) {
        return retrofit.create(RetrofitApi.class);
    }

    @Singleton
    @Provides
    RxNetErrorProcessor providerRxNetErrorProcessor() {
        return new RxNetErrorProcessor();
    }



}
