package cn.com.bigknow.trade.pos.Immediate.base.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by hujie on 16/6/20.
 */
public class SnackbarUtil {

    public static void makeSnack(View v, CharSequence msg) {
        if (msg == null || msg.length() == 0) {
            return;
        }
        Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show();
    }
}
