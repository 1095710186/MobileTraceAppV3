package cn.com.bigknow.trade.pos.Immediate.app.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by hujie on 16/6/13.
 */
public class CropImageUtil {


    //学员中心 裁剪的图片
    public static final String CROP_IMAGE_PATH = "/naodong/stucenter/cropImage";

    /**
     * 创建裁剪图片的地址
     * @return
     */
    public static File createCropImagePath() {
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            String rootPath = Environment.getExternalStorageDirectory().getPath();//获取跟目录
            File cropPath = new File(rootPath + CROP_IMAGE_PATH);
            if (!cropPath.exists()) {
                cropPath.mkdirs();
            }
            return cropPath;
        }

        return null;
    }

    /**
     * 获取裁剪图片的路径
     * @return
     */
    public static String getCropImagePath() {
        String rootPath = Environment.getExternalStorageDirectory().getPath();//获取跟目录
        return rootPath + CROP_IMAGE_PATH;
    }


    /**
     * 根据远程OssKey查找本地的图片的位置
     * @param ossObjectKey oss保存的地址
     * @return
     */
    public static String findLocalCropImageFromOssKey(String ossObjectKey) {

        int lastIndexOf = ossObjectKey.lastIndexOf("/");
        String cropImageName = ossObjectKey.substring(lastIndexOf + 1);
        File file = new File(getCropImagePath(), cropImageName);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        return null;
    }


    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileFormat(String fileName) {
        if (fileName == null || fileName.trim().length() == 0) return "";
        int point = fileName.lastIndexOf('.');
        return fileName.substring(point + 1);
    }

}
