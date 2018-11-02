package cn.com.bigknow.trade.pos.Immediate.app.di;

import android.app.Application;


import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import cn.com.bigknow.trade.pos.Immediate.BuildConfig;
import cn.com.bigknow.trade.pos.Immediate.app.util.BugtagsManager;
import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by hujie on 16/6/22.
 *
 * 第三个初始化管理
 */
public class ThirdPartyManaer {

    @Inject
    public Application application;
   // public RefWatcher refWatcher;

    @Inject
    OkHttpClient okHttpClient;

    private static ThirdPartyManaer thirdPartyManaer;


    public static ThirdPartyManaer getInstance() {
        if (thirdPartyManaer == null) {
            thirdPartyManaer = new ThirdPartyManaer();
        }
        return thirdPartyManaer;
    }

    private ThirdPartyManaer() {

    }

    public void initThirdParty() {
        initWatcher();
        initBugTags();
        initFIR();
        initJpush();
    }


    private void initJpush()
    {
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(application);     		// 初始化 JPush
    }


    private void initFIR() {
       // FIR.init(application);
    }

    private void initBugTags() {
        BugtagsManager.start(application);
    }


    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void watcher(Object o) {
//        if (refWatcher != null) {
//            refWatcher.watch(o);
//        }
    }

    private void initWatcher() {
        if (BuildConfig.DEBUG) {
            Observable.timer(1, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread())
                    .subscribe(aLong -> {
                        //refWatcher = LeakCanary.install(application);
                    });
        }
    }
}
