package cn.com.bigknow.trade.pos.Immediate.model.api;

import cn.com.bigknow.trade.pos.Immediate.model.bean.BaseEntity;

import rx.functions.Func1;

/**
 * Created by hujie on 16/6/18.
 */
public class ResultFunc<T> implements Func1<BaseEntity<T>, T> {

    @Override
    public T call(BaseEntity<T> o) {
        return o.data;
    }
}
