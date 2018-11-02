package cn.com.bigknow.trade.pos.Immediate.base.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Created by hujie on 15/11/12.
 *
 * 兼容API 帮助类
 */

public class ViewHelper {


    public static float getAlpha(View view) {
        return view.getAlpha();
    }

    public static void setAlpha(View view, float alpha) {
        view.setAlpha(alpha);
    }

    public static void setScaleX(View view, float scaleX) {
        view.setScaleX(scaleX);
    }

    public static void setScaleY(View view, float scaleY) {
        view.setScaleY(scaleY);
    }


    public static void setTranslationX(View view, float translationX) {
        view.setTranslationX(translationX);
    }

    public static void setTranslationY(View view, float translationY) {
        view.setTranslationY(translationY);
    }

    public static void setPivotX(View view, float pivotX) {
        view.setPivotX(pivotX);
    }

    public static void setPivotY(View view, float pivotY) {
        view.setPivotY(pivotY);
    }



    public static void setBackground(View iv, Drawable drawable) {
        if (Build.VERSION.SDK_INT >=16) {
            iv.setBackground(drawable);
        } else {
            iv.setBackgroundDrawable(drawable);
        }
    }


}
