package cn.com.bigknow.trade.pos.Immediate.app.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import cn.com.bigknow.trade.pos.Immediate.model.bean.YearMonthbean;

/**
 * 时间工具类
 */
public class DateUtils extends android.text.format.DateUtils {
    public static DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String getDateString(long time, DateFormat dateFormat) {
        Date date = new Date(time);
        return dateFormat.format(date);
    }

    public static String getCurrentDateString() {
        return getDateString(System.currentTimeMillis(), defaultDateFormat);
    }

    public static String getStringDateFromDate(Date date) {
        return defaultDateFormat.format(date);
    }

    public static String getCurrentDateString_() {
        return getDateString(System.currentTimeMillis(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }
    public static List<YearMonthbean> getCurrentDateBefo3Month() {
        List<YearMonthbean> list = new ArrayList<>() ;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(date);
        String[] item = time.split("-");
        int year = Integer.parseInt(item[0]);
        int month = Integer.parseInt(item[1]);
        int day = Integer.parseInt(item[2]);

        YearMonthbean ym1 = new YearMonthbean();
        YearMonthbean ym2 = new YearMonthbean();
        YearMonthbean ym3 = new YearMonthbean();
        int mon1,mon2,mon3;
        int year1,year2,year3;
        //前一个月
        if ((month -1) <= 0) {
            mon1 = month + 12 - 1;
            year1 = year - 1;
        } else {
            mon1 = month - 1;
            year1 = year;

        }

        //前2个月
        if ((month -2) <= 0) {
            mon2 = month + 12 - 2;
            year2 = year - 1;
        } else {
            mon2 = month - 2;
            year2 = year;
        }

        //前3个月
        if ((month - 3) <= 0) {
            mon3 = month + 12 - 3;
            year3 = year - 1;
        } else {
            mon3 = month - 3;
            year3 = year;

        }
        ym1.setYear(year1);
        ym1.setMonth(mon1);
        ym1.setDate(String.valueOf(year1).substring(2,4)+"年"+(mon1<10?("0"+mon1+"月"):(mon1+"月")));
        ym2.setYear(year2);
        ym2.setMonth(mon2);
        ym2.setDate(String.valueOf(year2).substring(2,4)+"年"+(mon2<10?("0"+mon2+"月"):(mon2+"月 ")));
        ym3.setYear(year3);
        ym3.setMonth(mon3);
        ym3.setDate(String.valueOf(year3).substring(2,4)+"年"+(mon3<10?("0"+mon3+"月"):(mon3+"月 ")));

        list.add(ym1);
        list.add(ym2);
        list.add(ym3);
//        time = year + "-" + month + "-" + day;
        return list;
    }
    //昨天
    public static String getYestoryDateString() {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -1);//
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        String dateString = defaultDateFormat.format(date);
        return dateString;
    }

    //前天
    public static String getThreeDateString() {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -2);//
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        String dateString = defaultDateFormat.format(date);
        return dateString;
    }


    public static String parseDate(String createTime) {
        if (TextUtils.isEmpty(createTime)) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date zt = calendar.getTime();

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date());
        calendar1.add(Calendar.DAY_OF_MONTH, -2);
        Date qt = calendar1.getTime();

        String ztString = sdf.format(zt);
        String qtString = sdf.format(qt);
        String jtString = sdf.format(new Date());

        if (jtString.equals(createTime)) {
            return "今天";
        } else if (ztString.equals(createTime)) {
            return "昨天";
        } else if (qtString.equals(createTime)) {
            return "前天";
        }
        return createTime;

    }


    /**
     * 按日加
     *
     * @param date
     * @param value
     * @return
     */
    public static Date addDay(Date date, int value) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.DAY_OF_YEAR, value);
        return now.getTime();
    }

    /**
     * 传入Date 定制日期格式 <br/>
     * default-format="yyyy-MM-dd"
     */
    @SuppressLint("SimpleDateFormat")
    public static String dateFormat(Date date) {
        return dateFormat(date, "yyyy-MM-dd");
    }

    /**
     * 传入Date 定制日期格式
     */
    @SuppressLint("SimpleDateFormat")
    public static String dateFormat(Date date, String format) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        }
        return "";
    }


    public static Date getDate(long time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String d = format.format(time);
        Date date = format.parse(d);
        return date;

    }

    public static String addSevenDay(int addDay) {
        String syllabusTime = DateUtils.dateFormat
                (DateUtils.addDay(new Date(System.currentTimeMillis()), addDay));
        return syllabusTime;
    }



    /**
     * 得到某年某周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        week = week - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);

        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * 得到某年某周的最后一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        week = week - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);
        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
//        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek()); // Sunday
        return calendar.getTime();
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
//        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek() + 6); // Saturday
        return calendar.getTime();
    }

    //Today
    public static String getToday() {
        //获取当前时间的DateTime对象，这个对象类似于java.util.Date的方法，用于封装当前时间对象
        DateTime now = DateTime.now();
        String toDay = DateFormatUtils.format(now.toDate(),"yyyy-MM-dd");
        //本周日
        System.out.println(toDay);//20161002
        LogUtil.v("toDay",toDay);
        return toDay;
    }
    //本周第一天
    public static String getFirstDayOfWeek() {
        //获取当前时间的DateTime对象，这个对象类似于java.util.Date的方法，用于封装当前时间对象
        DateTime now = DateTime.now();
        //设置当前时间所在的周的周一,withDayOfWeek(int day)类似于Calendar.set(MONDAY)，但比Calandar好用多了，Calandar
        //的时间是西方时间，他们的周日相当于我们的周一，用起来很别扭，DateTime就不会这样
        DateTime monday = now.withDayOfWeek(1)
        //设置日期为当天的某个小时，这里设置为0点
                .withHourOfDay(0)
        //设置日期为当天的某个分钟，设置为0分
                .withMinuteOfHour(0)
        //设置为当天的某秒。设置为0秒
                .withSecondOfMinute(0);
        String monDay = DateFormatUtils.format(monday.toDate(),"yyyy-MM-dd");
        //本周日
        System.out.println(monDay);//20161002
        LogUtil.v("MONDAY",monDay);
        return monDay;
    }

    /**
     * 本周周日
     *
     * @param
     * @return
     */
    public static String  getLastDayOfWeek() {
        DateTime now = DateTime.now();
        //如果要获取本周日的时间也很简单，将withDayOfWeek设置为7就行了
        now = now.withDayOfWeek(7).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
        Date date = now.toDate();
        String sunDay = DateFormatUtils.format(date,"yyyy-MM-dd");
        //本周日
        System.out.println(sunDay);//20161002
        LogUtil.v("SUNDAY",sunDay);
        return sunDay;
    }
    //本月第一天
    public static Date getFirstDayOfMonth_(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), 1);
        return calendar.getTime();
    }

    /**
     * 取得当前日期所在周的前一周最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfLastWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfWeek(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.WEEK_OF_YEAR) - 1);
    }

    /**
     * 返回指定日期的月的第一天
     *
     * @param
     * @param
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), 1);
        return calendar.getTime();
    }

    /**
     * 返回指定年月的月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的月的最后一天
     *
     * @param
     * @param
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回指定年月的月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的上个月的最后一天
     *
     * @param
     * @param
     * @return
     */
    public static Date getLastDayOfLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) - 1, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的季的第一天
     *
     * @param
     * @param
     * @return
     */
    public static Date getFirstDayOfQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getFirstDayOfQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 返回指定年季的季的第一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static Date getFirstDayOfQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 1 - 1;
        } else if (quarter == 2) {
            month = 4 - 1;
        } else if (quarter == 3) {
            month = 7 - 1;
        } else if (quarter == 4) {
            month = 10 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getFirstDayOfMonth(year, month);
    }

    /**
     * 返回指定日期的季的最后一天
     *
     * @param
     * @param
     * @return
     */
    public static Date getLastDayOfQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 返回指定年季的季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static Date getLastDayOfQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 3 - 1;
        } else if (quarter == 2) {
            month = 6 - 1;
        } else if (quarter == 3) {
            month = 9 - 1;
        } else if (quarter == 4) {
            month = 12 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }

    /**
     * 返回指定日期的上一季的最后一天
     *
     * @param
     * @param
     * @return
     */
    public static Date getLastDayOfLastQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfLastQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 返回指定年季的上一季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static Date getLastDayOfLastQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 12 - 1;
        } else if (quarter == 2) {
            month = 3 - 1;
        } else if (quarter == 3) {
            month = 6 - 1;
        } else if (quarter == 4) {
            month = 9 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }

    /**
     * 返回指定日期的季度
     *
     * @param date
     * @return
     */
    public static int getQuarterOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }

}
