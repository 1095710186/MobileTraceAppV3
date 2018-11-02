package cn.com.bigknow.trade.pos.Immediate.base.util;

import android.content.Context;
import android.os.Environment;

/**
 * Created by hujie on 16/6/23.
 */
public class FileUtil {

    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
    }


    /**
     * 获取存储路径,没有SD卡 就存储到本地路径下面
     * @param context
     * @return
     */
    private static String getRootCachePath(Context context) {

        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            return Environment.getExternalStorageDirectory().getPath();//获取跟目录
        } else {
            return context.getCacheDir().getPath();
        }
    }


    public static String getCachePath(Context context, String path) {
        return getRootCachePath(context) + path;
    }
}
