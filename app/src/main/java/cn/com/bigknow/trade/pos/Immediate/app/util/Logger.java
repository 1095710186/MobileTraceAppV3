package cn.com.bigknow.trade.pos.Immediate.app.util;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hujie on 16/6/6.
 */
public class Logger {



    private static final String LOGPATH = "/com.bigknow/Log";

    /**

     */
    public static void writeLog(String tag, String log) {
        try {
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
            if (sdCardExist) {
                String rootPath = Environment.getExternalStorageDirectory().getPath();//获取跟目录

                String logPath = rootPath + LOGPATH;


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                String fileName = sdf.format(new Date())+".txt";

                File dir = new File(logPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(logPath, fileName);
                if (!file.exists()) {
                    file.createNewFile();

                }


                FileWriter writer = new FileWriter(file, true);
                //writer.append();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                writer.write(sdf1.format(new Date()) + "    " + tag + "   :   " + log + "\r\n");
                writer.flush();
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
