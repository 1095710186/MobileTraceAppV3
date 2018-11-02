package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.util.List;

/**
 * Created by hujie on 2016/8/15..
 */


public class RequestWrap<T> {

    public String name;
    public List<T> data;

}
