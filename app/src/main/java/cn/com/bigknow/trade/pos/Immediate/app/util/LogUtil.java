package cn.com.bigknow.trade.pos.Immediate.app.util;

import android.util.Log;

/**
 * Log工具类
 * Created by Administrator on 2015/12/9.
 */
public class LogUtil {
    //当showLog为true时打印log（项目完成设置为false，不打印log）
    private static final boolean showLog = true;

    /**
     * 信息log
     * @param TAG
     * @param Msg
     */
   public static void i(String TAG, String Msg){
       if (showLog){
           Log.i(TAG,Msg);
       }
   }

    /**
     * 错误log
     * @param TAG
     * @param MSG
     */
    public static void e(String TAG, String MSG){
        if (showLog){
            Log.e(TAG,MSG);
        }
    }

    /**
     * 全部log
     * @param TAG
     * @param MSG
     */
    public static void v(String TAG, String MSG){
        if (showLog){
            Log.v(TAG,MSG);
        }
    }

    /**
     * debug log
     * @param TAG
     * @param MSG
     */
    public static void d(String TAG, String MSG){
        if (showLog){
            Log.d(TAG,MSG);
        }
    }

    /**
     * 警告 warn log
     * @param TAG
     * @param MSG
     */
    public static void w(String TAG, String MSG){
        if (showLog){
            Log.w(TAG,MSG);
        }
    }
}
