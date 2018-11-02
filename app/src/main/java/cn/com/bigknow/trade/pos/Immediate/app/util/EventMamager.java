package cn.com.bigknow.trade.pos.Immediate.app.util;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import cn.com.bigknow.trade.pos.Immediate.model.bean.SimpleEvent;

/**
 * Created by hujie on 16/6/22.
 */
public class EventMamager {


    //注入自定义的eventbus为全局的
    @Inject
    public EventBus eventBus;

    private static EventMamager eventMamager;


    public static EventMamager getInstance() {
        if (eventMamager == null) {
            eventMamager = new EventMamager();
        }
        return eventMamager;
    }

    private EventMamager() {

    }

    public void postIntEvent(int type, int intEvent) {
        eventBus.post(new SimpleEvent(type, intEvent));
    }

    public void postStringEvent(int type, String strEvent) {
        eventBus.post(new SimpleEvent(type, strEvent));
    }

    public void postString_2Event(int type, String strEvent,String idEvent) {
        eventBus.post(new SimpleEvent(type, strEvent,idEvent));
    }

    public void postObjEvent(int type, Object objectEvent) {
        eventBus.post(new SimpleEvent(type, objectEvent));
    }

    public void postEvent(int type) {
        eventBus.post(new SimpleEvent(type));
    }

    public void postStickyEvent(int type)
    {
        eventBus.postSticky(new SimpleEvent(type));
    }
}
