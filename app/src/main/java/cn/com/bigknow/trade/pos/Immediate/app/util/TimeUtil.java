package cn.com.bigknow.trade.pos.Immediate.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hujie on 16/6/21.
 */
public class TimeUtil {


    /**
     * 讲解时间
     * @return
     */
    public static String convertToExplainTime(long explainTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("M月d日  HH:mm");
        return sdf.format(new Date(explainTime * 1000));
    }

    /**
     * 计时格式化为HH:MM:SS 或者mm:ss
     * @param miss
     * @return
     */
    public static String formatMiss1(int miss) {
        miss = miss / 1000;
        String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
        String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0" + (miss % 3600) / 60;
        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0" + (miss % 3600) % 60;
        if (hh.equals("00")) {
            return mm + ":" + ss;
        }
        return hh + ":" + mm + ":" + ss;
    }

    public static String beforeTime(long time) {
        long currentSeconds = System.currentTimeMillis() / 1000;
        long interval = currentSeconds - time;// 与现在时间相差秒数
        String timeStr = null;
        if (interval >= 30 * 24 * 60 * 60) {// 30天以上 显示具体时间
            timeStr = new SimpleDateFormat("MM-dd HH:mm").format(new Date(time));
        } else if (interval > 24 * 60 * 60 && interval < 30 * 24 * 60 * 60) {// 1天以上
            timeStr = interval / (24 * 60 * 60) + "天前";
        } else if (interval > 60 * 60) {// 1小时-24小时
            timeStr = interval / (60 * 60) + "小时前";
        } else if (interval > 60) {// 1分钟-59分钟
            timeStr = interval / 60 + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }
}
