package cn.com.bigknow.trade.pos.Immediate.model.api;

import android.content.Context;
import android.content.Intent;

import cn.com.bigknow.trade.pos.Immediate.app.BootstrapApp;
import cn.com.bigknow.trade.pos.Immediate.app.ui.LoginActivity;
import cn.com.bigknow.trade.pos.Immediate.base.util.ToastUtil;
import cn.com.bigknow.trade.pos.Immediate.model.errors.ApiError;

/**
 * Created by hujie on 16/6/18.
 */
public abstract class SimpleRequestListener<T> implements RequestListener<T> {


    public SimpleRequestListener() {
    }


    @Override
    public void onStart() {

    }


    @Override
    public void onFinish() {

    }

    // 重新登录
    private static void toLogin(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("reLogin", true);
        context.startActivity(intent);
    }

    @Override
    public void onError(ApiError error) {
        if (error.isApiError() && ("common.access.err_code".equals(error.errorCode)
                ||("common.connect.err_code").equals(error.errorCode))
                ||("common.conncect.err_code").equals(error.errorCode)) {
            toLogin(BootstrapApp.get());
            return;
        }
        ToastUtil.makeToast(error.toString());

    }
}
