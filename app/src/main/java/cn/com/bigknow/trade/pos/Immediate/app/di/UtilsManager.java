package cn.com.bigknow.trade.pos.Immediate.app.di;

import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;

import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.LogUtils;

import cn.com.bigknow.trade.pos.Immediate.app.ui.LoginActivity;
import cn.com.bigknow.trade.pos.Immediate.app.util.Logger;
import cn.com.bigknow.trade.pos.Immediate.base.activitymanager.TheActivityManager;
import cn.com.bigknow.trade.pos.Immediate.base.util.CrashHandler;
import cn.com.bigknow.trade.pos.Immediate.base.util.ResourcesUtil;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;

import javax.inject.Inject;

/**
 * Created by hujie on 16/6/17.
 */
public class UtilsManager {

    @Inject
    public Application application;
    @Inject
    public Resources resources;


    private static UtilsManager utilsManager;


    public static UtilsManager getInstance() {
        if (utilsManager == null) {
            utilsManager = new UtilsManager();
        }
        return utilsManager;
    }

    private UtilsManager() {

    }


    public void initUtils() {
        initActivityManager();
        initResources();
        initToastUtil();
        initCrashHanlder();
        initLogUtil();
    }

    /**
     * 配置资源
     */
    private void initResources() {
        ResourcesUtil.initResources(resources);
    }


    /**
     * 初始化toast
     */
    private void initToastUtil() {
        ToastUtil.initToast(application);
    }


    /**
     * 崩溃监听
     */
    private void initCrashHanlder() {
        CrashHandler.getInstance().init(application, (debug, e) -> {
            LogUtils.e(e.toString());
            Logger.writeLog("崩溃", e.toString());
//            if (!debug) {
                TheActivityManager.getInstance().finishAll();
                /*Intent intent = new Intent();
                intent.setClass(application, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("reLogin", true);
                application.startActivity(intent);*/

//            }
        });
    }

    /**
     * activity 管理
     */
    private void initActivityManager() {
        TheActivityManager.getInstance().configure(application);
    }

    /**
     * 日志配置
     */
    private void initLogUtil() {
        LogUtils.getLogConfig()
                .configAllowLog(true)
                .configTagPrefix("过")
                .configShowBorders(true)
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")
                .configLevel(LogLevel.TYPE_VERBOSE);
    }
}
