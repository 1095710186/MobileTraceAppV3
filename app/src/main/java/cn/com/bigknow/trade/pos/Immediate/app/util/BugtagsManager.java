package cn.com.bigknow.trade.pos.Immediate.app.util;

import android.app.Activity;
import android.app.Application;
import android.view.MotionEvent;

import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;

import cn.com.bigknow.trade.pos.Immediate.BuildConfig;

/**
 * Created by hujie on 16/1/20.
 */
public class BugtagsManager {


    //是否开启bugtags
    public static boolean openBugtags() {
        return true;
    }

    private static boolean hasInit = false;


    public static void start(Application context) {
        if (openBugtags()) {
            BugtagsOptions.Builder builder = new BugtagsOptions.Builder();
            builder.trackingLocation(false);
            if (BuildConfig.DEBUG) {
                Bugtags.start("868c7bdc8e0a7a31d77b79a814bf6226", context, Bugtags.BTGInvocationEventBubble, builder.build());
            } else {
                Bugtags.start("05da22155c6f47ad372f9711da790cfd", context, Bugtags.BTGInvocationEventNone, builder.build());
            }
            hasInit = true;
        }
    }

    public static void onResume(Activity activity) {
        if (openBugtags() && hasInit) {
            //  Bugtags.onResume(activity);
            // OneApmAnalysis.onResume();
            Bugtags.onResume(activity);

        }
    }

    public static void onPause(Activity activity) {

        if (openBugtags() && hasInit) {
            Bugtags.onPause(activity);

        }
    }

    public static void onDispatchTouchEvent(Activity activity, MotionEvent event) {
        if (openBugtags() && hasInit) {
            Bugtags.onDispatchTouchEvent(activity, event);
        }
    }
}
