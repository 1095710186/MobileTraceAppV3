package cn.com.bigknow.trade.pos.Immediate.app.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import cn.com.bigknow.trade.pos.Immediate.app.BootstrapApp;

/**
 * Created by hujie on 15/12/8.
 */
public class GlideHelper {


    static final int TYPE_URL = 1;
    static final int TYPE_FILE = 2;

    private static boolean isDestory(Context context) {
        if (context != null) {
            if (context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    return true;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (((Activity) context).isDestroyed()) {
                        return true;
                    }
                }
            }
        } else {
            return true;
        }
        return false;
    }


    public static DrawableRequestBuilder loadRequestBuilder(Context context, String url) {
        if (isDestory(context)) {
            context = BootstrapApp.get();
        }
        return Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL);
    }


    //文件不需要再次缓存
    public static DrawableRequestBuilder loadRequestBuilder(Context context, File file) {
        if (isDestory(context)) {
            context = BootstrapApp.get();
        }
        return Glide.with(context).load(file).diskCacheStrategy(DiskCacheStrategy.NONE);
    }

    public static DrawableRequestBuilder loadRequestBuilder(Context context, String url, boolean centerCrop, boolean crossFade) {
        if (isDestory(context)) {
            context = BootstrapApp.get();
        }
        if (centerCrop && crossFade) {
            return loadRequestBuilder(context, url).centerCrop().crossFade();
        } else {
            if (centerCrop) {
                return loadRequestBuilder(context, url).centerCrop().dontAnimate();
            } else if (crossFade) {
                return loadRequestBuilder(context, url).crossFade();
            } else {
                return loadRequestBuilder(context, url).dontAnimate();
            }
        }

    }

    public static DrawableRequestBuilder loadRequestBuilder(Context context, File file, boolean centerCrop, boolean crossFade) {
        if (isDestory(context)) {
            context = BootstrapApp.get();
        }
        if (centerCrop && crossFade) {
            return loadRequestBuilder(context, file).centerCrop().crossFade();
        } else {
            if (centerCrop) {
                return loadRequestBuilder(context, file).centerCrop().dontAnimate();
            } else if (crossFade) {
                return loadRequestBuilder(context, file).crossFade();
            } else {
                return loadRequestBuilder(context, file).dontAnimate();
            }
        }

    }

    public static void load(Context context, String path, boolean centerCrop, boolean crossFade, ImageView iv) {
        if (path == null) {
            return;
        }
        int type = path.matches("http+.*") ? TYPE_URL : TYPE_FILE;
        if (type == TYPE_URL) {
            loadRequestBuilder(context, path, centerCrop, crossFade).into(iv);
        } else {
            loadRequestBuilder(context, new File(path), centerCrop, crossFade).into(iv);
        }
    }


    public static DrawableRequestBuilder load(Context context, String path, boolean centerCrop, boolean crossFade) {

        int type = path.matches("http+.*") ? TYPE_URL : TYPE_FILE;
        if (type == TYPE_URL) {
            return loadRequestBuilder(context, path, centerCrop, crossFade);
        } else {
            return loadRequestBuilder(context, new File(path), centerCrop, crossFade);
        }
    }


    /**
     * 只取cache中的
     * @param context
     * @param path
     * @return
     */
    public static DrawableRequestBuilder loadOnlyCache(Context context, String path) {
        if (isDestory(context)) {
            context = BootstrapApp.get();
        }
        return Glide.with(context).using(cacheOnlyStreamLoader).load(path).diskCacheStrategy(DiskCacheStrategy.SOURCE);
    }


    public static final StreamModelLoader<String> cacheOnlyStreamLoader = new StreamModelLoader<String>() {
        @Override
        public DataFetcher<InputStream> getResourceFetcher(final String model, int i, int i1) {
            return new DataFetcher<InputStream>() {
                @Override
                public InputStream loadData(Priority priority) throws Exception {
                    throw new IOException();
                }

                @Override
                public void cleanup() {

                }

                @Override
                public String getId() {
                    return model;
                }

                @Override
                public void cancel() {

                }
            };
        }
    };





    /**
     * 加载头像
     * @param context
     * @param url
     * @param iv
     */
    public static void loadHeadImage(Context context, String url, ImageView iv) {
        loadRequestBuilder(context, url, true, false).into(iv);
    }


}
