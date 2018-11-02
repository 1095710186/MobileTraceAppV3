package cn.com.bigknow.trade.pos.Immediate.base.util;

import android.widget.TextView;

/**
 * Created by hujie on 16/6/19.
 */
public class ViewUtil {


    public static String getText(boolean trim, TextView textView) {
        String text = textView.getText().toString();
        if (trim) {
            text = text.trim();
        }
        return text;
    }

    public static boolean isTextNull(TextView textView) {
        if (textView.getText().toString().trim().length() == 0) {
            return true;
        }
        return false;
    }
}
