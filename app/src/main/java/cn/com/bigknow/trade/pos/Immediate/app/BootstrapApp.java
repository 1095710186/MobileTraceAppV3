package cn.com.bigknow.trade.pos.Immediate.app;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;

import cn.com.bigknow.trade.pos.Immediate.R;
import cn.com.bigknow.trade.pos.Immediate.app.di.AppComponent;
import cn.com.bigknow.trade.pos.Immediate.app.di.DaggerAppComponent;
import cn.com.bigknow.trade.pos.Immediate.app.di.ThirdPartyManaer;
import cn.com.bigknow.trade.pos.Immediate.app.di.UtilsManager;
import cn.com.bigknow.trade.pos.Immediate.app.util.EventMamager;
import cn.com.bigknow.trade.pos.Immediate.base.di.AppModule;

/**
 * Created by hujie on 16/6/13.
 */
public class BootstrapApp extends Application {

    AppComponent appComponent;

    private static BootstrapApp app;

    public boolean isMessage = false;

    public boolean isMessage() {
        return isMessage;
    }

    public void setMessage(boolean message) {
        isMessage = message;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appComponent = createAppComponent();
        initInject();
    //  <!-- 请替换成在语音云官网申请的appid --> 科大讯飞
        SpeechUtility.createUtility(BootstrapApp.this, "appid=5812c770");

    }

    /**
     *
     */
    private void initInject() {
        appComponent.inject(EventMamager.getInstance());
        appComponent.inject(ThirdPartyManaer.getInstance());
        appComponent.inject(UtilsManager.getInstance());
        UtilsManager.getInstance().initUtils();
        ThirdPartyManaer.getInstance().initThirdParty();

    }

    public static BootstrapApp get() {
        return app;
    }

    private AppComponent createAppComponent() {
        return DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }


    public AppComponent appComponent() {
        return appComponent;
    }

}
