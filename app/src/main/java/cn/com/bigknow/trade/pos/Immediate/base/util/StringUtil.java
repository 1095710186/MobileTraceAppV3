package cn.com.bigknow.trade.pos.Immediate.base.util;

import android.renderscript.Float2;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.bigknow.trade.pos.Immediate.app.util.LogUtil;

/**
 * Created by
 */


public class StringUtil {

    public static String getString(String str) {
        if (TextUtils.isEmpty(str) || "null".equals(str)) {
            return "";
        } else {
            return str;
        }
    }


    BigDecimal bd;
    static DecimalFormat df = new java.text.DecimalFormat("#.##");
    static DecimalFormat df3 = new java.text.DecimalFormat("#.###");
    private static int MAX_ENGTH = 10;
    public static String getResultString(int type,String resultString){
        String toStirng = resultString;
        if(isTrueDecimal(resultString)){

            if (resultString.length() > MAX_ENGTH ){
                String aaa = resultString.substring(0,MAX_ENGTH);
                String [] re = aaa.split("\\.");
                if (re.length==1){
                    toStirng = re[0];
                }else if (re.length ==2){
                    if (type == 0){
                        if (re[1].length()>2){
                            toStirng = re[0]+"."+re[1].substring(0,2);
                        }else {
                            toStirng = aaa;
                        }
                    }else if (type == 1){
                        if (re[1].length()>3){
                            toStirng = re[0]+"."+re[1].substring(0,3);
                        }else {
                            toStirng = aaa;
                        }
                    }
                }
                LogUtil.d("REPOS_XS_B1_M",toStirng);

            }else {
                String [] re_ = resultString.split("\\.");
                if (re_.length==1){
                    toStirng = re_[0];
                    LogUtil.d("REPOS_XS_B1",toStirng);
                }else if (re_.length ==2) {
                    if (type == 0) {
                        if (re_[1].length()>2){

                            toStirng = re_[0]+"."+re_[1].substring(0,2);
                            LogUtil.d("REPOS_XS_B2_0",toStirng);
                        }else {
                            toStirng = resultString;
                            LogUtil.d("REPOS_XS_B2_1",toStirng);
                        }
//                        toStirng = df.format(Float.parseFloat(resultString));
                    } else if (type == 1) {
                        if (re_[1].length()>3){
                            toStirng = re_[0]+"."+re_[1].substring(0,3);
                            LogUtil.d("REPOS_XS_B3_0",toStirng);
                        }else {
                            toStirng = resultString;
                            LogUtil.d("REPOS_XS_B3_1",toStirng);
                        }
//                        toStirng = df3.format(Float.parseFloat(resultString));
                    }
                }
            }

        }else {
            if (resultString.length()>MAX_ENGTH){
                toStirng = resultString.substring(0,MAX_ENGTH);
            }else {
                toStirng = resultString;
            }

        }
        return toStirng;
    }
    public static boolean isTrueDecimal(String str){
        //采用正则表达式的方式来判断一个字符串是否为数字，这种方式判断面比较全
        //可以判断正负、整数小数
        String s = "-?[0-9]+.*[0-9]*";
        //小数
        String decimal = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";//
        Boolean strResult = str.matches(decimal);
        if(strResult == true) {
            System.out.println("Is Number!");
        } else {
            System.out.println("Is not Number!");
        }
        return strResult;
    }
    public static boolean isTrue(String type,String str){
        //java判断一段字符串中是否包含特殊的字符
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(type);
        System.out.println(m.find());
        return m.find();
    }
}
