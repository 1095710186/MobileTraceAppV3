package cn.com.bigknow.trade.pos.Immediate.model.api;

import rx.functions.Action0;

/**
 * Created by hujie on 16/6/18.
 */
public class CompleteAction implements Action0 {

    RequestListener requestListener;

    public CompleteAction(RequestListener requestListener) {
        this.requestListener = requestListener;
    }
    @Override
    public void call() {
        requestListener.onFinish();
    }
}
