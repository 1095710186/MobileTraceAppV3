package cn.com.bigknow.trade.pos.Immediate.app.di;

import cn.com.bigknow.trade.pos.Immediate.model.api.Api;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by hujie on 16/6/18.
 */
@Singleton
@Subcomponent(modules = ApiModule.class)
public interface ApiComponent {
    void inject(Api api);
}
