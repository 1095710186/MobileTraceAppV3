package cn.com.bigknow.trade.pos.Immediate.model.api;

import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

/**
 * Created by hujie on 16/6/18.
 */
public interface RequestListener<T> {

    void onStart();
    void onSuccess(T t);
    void onFinish();
    void onError(ApiError error);
}
