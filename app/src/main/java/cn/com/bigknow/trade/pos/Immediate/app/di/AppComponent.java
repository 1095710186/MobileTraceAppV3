package cn.com.bigknow.trade.pos.Immediate.app.di;

import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.app.util.UserManager;
import cn.com.bigknow.trade.pos.Immediate.base.di.AppModule;
import cn.com.bigknow.trade.pos.Immediate.base.provider.ProviderModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by hujie on 16/6/17.
 */
@Singleton
@Component(modules = {AppModule.class, ProviderModule.class, ProviderConfigModule.class})
public interface AppComponent {

    void inject(UtilsManager utilsManager);

    void inject(UserManager userManager);

    void inject(EventMamager eventBusUitl);

    void inject(ThirdPartyManaer thirdPartyManaer);




    ApiComponent apiComponent();

}
