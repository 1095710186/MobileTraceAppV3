package cn.com.bigknow.trade.pos.Immediate.model.bean;

/**
 * Created by hujie on 16/6/23.
 */
public class SimpleEvent {

    public int type;

    public Object objectEvent;
    public int intEvent;
    public String strEvent;

    public String idEvent;

    public SimpleEvent(int type, int intEvent) {
        this.type = type;
        this.intEvent = intEvent;
    }

    public SimpleEvent(int type) {
        this.type = type;
    }


    public SimpleEvent(int type, String strEvent) {
        this.type = type;
        this.strEvent = strEvent;
    }

    public SimpleEvent(int type, String strEvent,String idEvent) {
        this.type = type;
        this.strEvent = strEvent;
        this.idEvent = idEvent;
    }


    public SimpleEvent(int type, Object objectEvent) {
        this.type = type;
        this.objectEvent = objectEvent;
    }


}
