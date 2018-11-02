package cn.com.bigknow.trade.pos.Immediate.app.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

/**
 * Created by hujie on 16/6/23.
 */
public class ImageLoader {

    public static void loadImage(Context context, String url, ImageView view) {
        if (url == null || url.isEmpty()) {
            return;
        }
        String type = url.matches("http+.*") ? "URL" : "FILE";
        if ("URL".equals(type)) {
            Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().crossFade().into(view);
        } else {
            Glide.with(context).load(new File(url)).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().crossFade().into(view);
        }
    }

    /**
     * 获取图片的File句柄
     * @param context
     * @param url
     */
    public static void loadImageFile(Context context, String url, final ImageLoadListener imageLoadListener) {
        if (url == null || url.isEmpty()) {
            return;
        }
        String type = url.matches("http+.*") ? "URL" : "FILE";
        if ("URL".equals(type)) {

            Glide.with(context).load(url).downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    imageLoadListener.onLoadComplete(resource);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    imageLoadListener.onLoadFail();
                }
            });
        } else {
            imageLoadListener.onLoadComplete(new File(url));
        }

    }

}
