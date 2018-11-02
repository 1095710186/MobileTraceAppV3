package cn.com.bigknow.trade.pos.Immediate.app.util;

import android.content.Context;

import java.io.File;

public class FileSizeUtil {


    /**
     * 获取所有缓存的文件的大小
     * 包含 图片,下载 crash,app目录下的cache files
     * @param context
     * @return
     */
    public static void clearAllCacheSize(Context context) {

        String cachePath = context.getCacheDir().getPath();
        String filePath = context.getFilesDir().getPath();

        deleteFile(cachePath);
        deleteFile(filePath);


    }


    public static void deleteFile(File oldPath) {
        if (oldPath.isDirectory()) {
            File[] files = oldPath.listFiles();
            for (File file : files) {
                deleteFile(file);
                file.delete();
            }
        } else {
            oldPath.delete();
        }
    }

    public static void deleteFile(String oldPath) {
        File oldFile = new File(oldPath) ;
        if(oldFile.exists()) {
            if (oldFile.isDirectory()) {
                File[] files = oldFile.listFiles();
                for (File file : files) {
                    deleteFile(file);
                    file.delete();
                }
            } else {
                oldFile.delete();
            }
        }
    }

}