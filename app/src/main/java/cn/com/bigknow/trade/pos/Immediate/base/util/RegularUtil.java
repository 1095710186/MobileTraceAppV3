package cn.com.bigknow.trade.pos.Immediate.base.util;

import java.util.regex.Pattern;

/**
 * Created by hujie on 16/8/9.
 */
public class RegularUtil {


    public final static Pattern phonePattern = Pattern.compile("^1[3|4|5|7|8]\\d{9}$");
    public final static Pattern chepaiPattern = Pattern.compile("^[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$");

    public static boolean isPhoneNum(String phoneNum) {
        return phonePattern.matcher(phoneNum).matches();
    }

    public static boolean isChePai(String chePai) {
        return chepaiPattern.matcher(chePai).matches();
    }
}
