package cn.com.bigknow.trade.pos.Immediate.base.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 媒体类型工具包
 * @author  @Cundong
 * @weibo http://weibo.com/liucundong
 * @blog http://www.liucundong.com
 * @date Apr 29, 2011 2:50:48 PM
 * @version 1.0
 */
public class MediaUtils {
    private static Map<String, String> FORMAT_TO_CONTENTTYPE = new HashMap<>();

    static {
        //音频
        FORMAT_TO_CONTENTTYPE.put("mp3", "audio");
        FORMAT_TO_CONTENTTYPE.put("mid", "audio");
        FORMAT_TO_CONTENTTYPE.put("midi", "audio");
        FORMAT_TO_CONTENTTYPE.put("asf", "audio");
        FORMAT_TO_CONTENTTYPE.put("wm", "audio");
        FORMAT_TO_CONTENTTYPE.put("wma", "audio");
        FORMAT_TO_CONTENTTYPE.put("wmd", "audio");
        FORMAT_TO_CONTENTTYPE.put("amr", "audio");
        FORMAT_TO_CONTENTTYPE.put("wav", "audio");
        FORMAT_TO_CONTENTTYPE.put("3gpp", "audio");
        FORMAT_TO_CONTENTTYPE.put("mod", "audio");
        FORMAT_TO_CONTENTTYPE.put("mpc", "audio");

        //视频
        FORMAT_TO_CONTENTTYPE.put("fla", "video");
        FORMAT_TO_CONTENTTYPE.put("flv", "video");
        FORMAT_TO_CONTENTTYPE.put("wav", "video");
        FORMAT_TO_CONTENTTYPE.put("wmv", "video");
        FORMAT_TO_CONTENTTYPE.put("avi", "video");
        FORMAT_TO_CONTENTTYPE.put("rm", "video");
        FORMAT_TO_CONTENTTYPE.put("rmvb", "video");
        FORMAT_TO_CONTENTTYPE.put("3gp", "video");
        FORMAT_TO_CONTENTTYPE.put("mp4", "video");
        FORMAT_TO_CONTENTTYPE.put("mov", "video");

        //flash
        FORMAT_TO_CONTENTTYPE.put("swf", "video");

        FORMAT_TO_CONTENTTYPE.put("null", "video");

        //图片
        FORMAT_TO_CONTENTTYPE.put("jpg", "photo");
        FORMAT_TO_CONTENTTYPE.put("jpeg", "photo");
        FORMAT_TO_CONTENTTYPE.put("png", "photo");
        FORMAT_TO_CONTENTTYPE.put("bmp", "photo");
        FORMAT_TO_CONTENTTYPE.put("gif", "photo");
    }

    /**
     * 根据根据扩展名获取类型
     * @param attFormat
     * @return
     */
    public static String getContentType(String attFormat) {
        String contentType = FORMAT_TO_CONTENTTYPE.get("null");

        if (attFormat != null) {
            contentType = FORMAT_TO_CONTENTTYPE.get(attFormat.toLowerCase());
        }
        return contentType;
    }

    /**
     * 根据文件绝对路径获取文件名
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (filePath==null) return "";
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * 判断文件MimeType的method
     * @param
     * @return
     */
    public static String getMIMEType(String filePath) {
        String type = "";
        String fName = getFileName(filePath);
        /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        
        /* 按扩展名的类型决定MimeType */
        switch (end) {
            case "m4a":
            case "mp3":
            case "mid":
            case "xmf":
            case "ogg":
            case "wav":
                type = "audio";
                break;
            case "3gp":
            case "mp4":
                type = "video";
                break;
            case "jpg":
            case "gif":
            case "png":
            case "jpeg":
            case "bmp":
                type = "image";
                break;
            case "doc":
            case "docx":
                type = "application/msword";
                break;
            case "xls":
                type = "application/vnd.ms-excel";
                break;
            case "ppt":
            case "pptx":
            case "pps":
            case "dps":
                type = "application/vnd.ms-powerpoint";
                break;
            default:
                type = "*";
                break;
        }
        /* 如果无法直接打开，就弹出软件列表给用户选择 */
        type += "/*";
        return type;
    }

    public static String getContentImagePath(Context context, Uri uri) {
        final String[] columns = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;

        Cursor imagecursor = MediaStore.Images.Media.query(context.getContentResolver(),
                uri, columns,
                null, null, orderBy);


        String path = null;
        if (imagecursor != null && imagecursor.getCount() > 0) {

            while (imagecursor.moveToNext()) {


                int dataColumnIndex = imagecursor
                        .getColumnIndex(MediaStore.Images.Media.DATA);

                path = imagecursor.getString(dataColumnIndex);

            }
            imagecursor.close();
        }
        return path;
    }

}