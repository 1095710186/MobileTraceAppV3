package cn.com.bigknow.trade.pos.Immediate.base.util;

import android.content.res.Resources;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.res.ResourcesCompat;
import android.util.TypedValue;

/**
 * Created by hujie on 16/6/16.
 */
public class ResourcesUtil {

    private static Resources resources;

    public static void initResources(Resources resources) {
        ResourcesUtil.resources = resources;
    }

    public static int getDimensionPixelSize(@DimenRes int dimenRes) {
        return resources.getDimensionPixelSize(dimenRes);
    }

    public static int dp2px(int dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics()) + 0.5);
    }

    public static int px2dp(int px) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, resources.getDisplayMetrics()) + 0.5);
    }


    public static int getColor(@ColorRes int colorId) {
        return ResourcesCompat.getColor(resources, colorId, resources.newTheme());
    }

    public static int screenWidth() {
        return resources.getDisplayMetrics().widthPixels;
    }


}
