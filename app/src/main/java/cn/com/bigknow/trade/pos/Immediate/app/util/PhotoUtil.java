package cn.com.bigknow.trade.pos.Immediate.app.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageLoader;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.File;

import cn.com.bigknow.trade.pos.Immediate.R;

/**
 Created by hujie on 14/12/17.
 */
public class PhotoUtil {


    public static final int REQUESTCODE_CHOICE_PHOTO = 10001; //相册
    public static final int REQUESTCODE_TAKE_PHOTO = 10002; //照相机

    /**
     选择图片
     @param mActivity
     */
    public static void choicePhoto(Activity mActivity) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media
                    .EXTERNAL_CONTENT_URI);
            intent.setType("image/*");

        }
        mActivity.startActivityForResult(intent, REQUESTCODE_CHOICE_PHOTO);
    }


    /**
     选择图片
     @param fragment
     */
    public static void choicePhoto(Fragment fragment) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media
                    .EXTERNAL_CONTENT_URI);
            intent.setType("image/*");

        }
        fragment.startActivityForResult(intent, REQUESTCODE_CHOICE_PHOTO);
    }


    private static String getImageCachePath(Context context) {
        return String.format("%s/photo", context.getExternalCacheDir().getAbsolutePath());
    }

    /**
     拍照并返回临时图片地址

     @param mActivity
     */
    public static String takePhoto(Activity mActivity) {


        String tempPhotoPath = null;
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File(getImageCachePath(mActivity));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "xgs_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        tempPhotoPath = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        mActivity.startActivityForResult(openCameraIntent, REQUESTCODE_TAKE_PHOTO);
        return tempPhotoPath;

    }

    /**
     拍照并返回临时图片地址

     @param fragment
     */
    public static String takePhoto(Fragment fragment) {

        String tempPhotoPath = null;
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File(getImageCachePath(fragment.getActivity()));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "xgs_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        tempPhotoPath = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        fragment.startActivityForResult(openCameraIntent, REQUESTCODE_TAKE_PHOTO);
        return tempPhotoPath;

    }


    public interface OnTakePhotoListener {
        void onTakePhoto(String path);
    }

    /**
     * 显示相册还是拍照对话框
     */
    public static void showChooicePhotoDialog(final Activity context, final OnTakePhotoListener takePhotoListener) {
        String[] ss = {"相册", "拍照"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setItems(ss, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    PhotoUtil.choicePhoto(context);
                }
                if (which == 1) {
                    takePhotoListener.onTakePhoto(PhotoUtil.takePhoto(context));
                }
            }
        });
        builder.show();
    }


    /**
     *  调用第三方图片选择框架
     * @param activity 在Activity中调用
     * @param single 是否单选
     *
     * @param maxNum 最多选几张
     */
    public static void open(Activity activity, boolean single, int maxNum) {


        ImageConfig.Builder builder = new ImageConfig.Builder(new GlideLoader())
                .steepToolBarColor(activity.getResources().getColor(R.color.colorPrimary))
                .titleBgColor(activity.getResources().getColor(R.color.colorPrimary))
                .showCamera()
                .titleSubmitTextColor(activity.getResources().getColor(R.color.default_text_color))
                .filePath(getImageCachePath(activity));
        if (single) {
            builder.singleSelect();
        } else {
            if (maxNum > 0) {
                builder.mutiSelectMaxSize(maxNum);
            }
            builder.mutiSelect();
        }
        ImageSelector.open(activity, builder.build(), ImageSelectorActivity.class);


    }


    /**
     *  调用第三方图片选择框架
     * @param fragment 在Fragment中调用
     * @param single 是否单选
     *
     * @param maxNum 最多选几张
     */
    public static void open(Fragment fragment, boolean single, int maxNum) {
        ImageConfig.Builder builder = new ImageConfig.Builder(new GlideLoader())
                .steepToolBarColor(fragment.getResources().getColor(R.color.colorPrimary))
                .titleBgColor(fragment.getResources().getColor(R.color.colorPrimary))
                .showCamera()
                .titleSubmitTextColor(fragment.getResources().getColor(R.color.default_text_color))
                .filePath(getImageCachePath(fragment.getContext()));
        if (single) {
            builder.singleSelect();
        } else {
            if (maxNum > 0) {
                builder.mutiSelectMaxSize(maxNum);
            }
            builder.mutiSelect();
        }
        ImageSelector.open(fragment, builder.build(), ImageSelectorActivity.class);


    }

    static class GlideLoader implements ImageLoader {

        @Override
        public void displayImage(Context context, String path, int size, ImageView imageView) {
            Glide.with(context).load(new File(path)).asBitmap().placeholder(R.mipmap.imageselector_photo).diskCacheStrategy(DiskCacheStrategy.NONE).override(size, size).centerCrop().into(imageView);
        }
    }

}
