package cn.com.bigknow.trade.pos.Immediate.base.util;

import android.content.Context;

import cn.com.bigknow.trade.pos.Immediate.base.widget.ProgressHUD;

/**
 * Created by hujie on 16/6/18.
 */
public class ProgressHudUtil {


    ProgressHUD progressHUD;
    Context context;

    public ProgressHudUtil(Context context) {
        this.context = context;
    }


    public void dismissProgressHud() {
        if (progressHUD != null) {
            progressHUD.dismiss();
        }
    }


    public void destory() {
        if (progressHUD != null) {
            if (progressHUD.isShowing()) {
                progressHUD.setOnDismissListener(dialog -> {
                    progressHUD = null;
                    context = null;
                });
                progressHUD.dismiss();
            } else {
                progressHUD = null;
                context = null;
            }

        }
    }


    public void showProgressHud(String message, boolean canCancle) {
        if (progressHUD != null && progressHUD.isShowing()) {
            return;
        }
        progressHUD = ProgressHUD.show(context, message, false, canCancle, null);
    }

    public void showProgressHud(boolean canCancle) {
        showProgressHud("", canCancle);
    }

    public void showProgressHud() {
        showProgressHud("", true);
    }

}
