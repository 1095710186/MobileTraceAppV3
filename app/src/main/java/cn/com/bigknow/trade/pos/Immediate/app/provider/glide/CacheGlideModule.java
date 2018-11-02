package cn.com.bigknow.trade.pos.Immediate.app.provider.glide;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by hujie on 15/12/9.
 * 缓存配置
 */
public class CacheGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

        if (glideImageCachePath(context) != null) {
            builder.setDiskCache(new DiskLruCacheFactory(glideImageCachePath(context), maxCacheSize()));
        }

    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }

    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
    }


    /**
     * 获取缓存大小
     * @return
     */
    private static int maxCacheSize() {
        int maxSize = 500 * 1024 * 1024;
        if (isSdCardExist()) {
            //最大500M
            maxSize = 1024 * 1024 * 1024;
        }
        return maxSize;
    }

    /**
     * 获取缓存地址
     * @param context
     * @return
     */
    private static String glideImageCachePath(Context context) {
        if (context.getExternalFilesDir(null) != null) {
            return context.getExternalFilesDir(null).getAbsolutePath() + "/imagecache";
        }
        return null;
    }
}
