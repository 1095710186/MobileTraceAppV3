package cn.com.bigknow.trade.pos.Immediate.base.util;

import android.app.Application;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by hujie on 16/6/15.
 */
public class ToastUtil {

    private static Toast mToast;

    private static Application applicationContext;

    public static void initToast(Application application) {
        applicationContext = application;
    }

    private static Toast createToast() {
        if (mToast == null) {
            mToast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT);
        }
        return mToast;
    }

    public static void cancleToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    public static void makeToast(CharSequence text) {
        if (text == null || text.length() == 0) {
            return;
        }
        mToast = createToast();
        mToast.setText(text);
        mToast.show();
    }

    public static void makeToast(@StringRes int textId) {
        if (textId != 0) {
            makeToast(applicationContext.getResources().getString(textId));
            return;
        }
    }


}
