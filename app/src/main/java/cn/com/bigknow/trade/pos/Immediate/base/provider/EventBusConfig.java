package cn.com.bigknow.trade.pos.Immediate.base.provider;


import org.greenrobot.eventbus.meta.SubscriberInfoIndex;

import cn.com.bigknow.trade.pos.Immediate.BuildConfig;

/**
 * Created by hujie on 16/6/18.
 */
public class EventBusConfig {


    private static final boolean DEBUG = BuildConfig.DEBUG;

    private boolean logNoSubscriberMessages;
    private boolean sendNoSubscriberEvent;
    private boolean eventInheritance;
    private boolean throwSubscriberException;
    private boolean ignoreGeneratedIndex;
    private SubscriberInfoIndex infoIndex;


    public void setInfoIndex(SubscriberInfoIndex infoIndex) {
        this.infoIndex = infoIndex;
    }

    public SubscriberInfoIndex getInfoIndex() {
        return infoIndex;
    }

    public boolean isIgnoreGeneratedIndex() {
        return !DEBUG;
    }

    public boolean isLogNoSubscriberMessages() {
        return DEBUG;
    }

    public boolean isSendNoSubscriberEvent() {
        return DEBUG;
    }


    public boolean isThrowSubscriberException() {
        return DEBUG;
    }
}
