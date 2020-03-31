package com.ztyb.framework.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间处理工具类
 */

public class TimeUtil {

    /**
     * 时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 时间格式：yyyy-MM-dd
     */
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 时间格式：yyyy.MM.dd
     */
    public static final SimpleDateFormat DATE_FORMAT_POINT = new SimpleDateFormat("yyyy.MM.dd");
    /**
     * 时间格式：yyyy.MM.dd HH:MM:SS
     */
    public static final SimpleDateFormat DATE_FORMAT_POINT_DETAIL = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    /**
     * 时间格式：yyyy-MM-dd HH:MM:SS
     */
    public static final SimpleDateFormat DATE_FORMAT_ABS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 时间格式：yyyy年MM月
     */
    public static final SimpleDateFormat YEAR_MOUTH = new SimpleDateFormat("yyyy年MM月");
    /**
     * 时间格式：yyyyMMddHHmmss
     */
    public static final SimpleDateFormat DATE_FORMAT_FILE = new SimpleDateFormat("yyyyMMddHHmmss");
    /**
     * 时间格式：yyyy-MM-dd HH:mm
     */
    public static final SimpleDateFormat DATE_FORMAT_HOUR_MIN = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private TimeUtil() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(timeInMillis);
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * long time to string, format is {@link #DATE_FORMAT_DATE}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTimes(long timeInMillis) {
        return getTime(timeInMillis, DATE_FORMAT_DATE);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    public static String replaceOtherDate(String dateStr) {
        if (!TextUtils.isEmpty(dateStr)) {
            try {
                dateStr = dateStr.substring(0, 19);
                Date date = DATE_FORMAT_ABS.parse(dateStr);
                dateStr = DATE_FORMAT_POINT.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dateStr;
    }

    public static String replaceOtherDate(Date date) {
        String dateStr = null;
        try {
            dateStr = DATE_FORMAT_POINT_DETAIL.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static String replaceABCDate(Date date) {
        String dateStr = null;
        try {
            dateStr = DATE_FORMAT_ABS.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static boolean compareDate(String startTime, String endTime) {
        try {
            Date st = DATE_FORMAT_ABS.parse(startTime);
            Date et = DATE_FORMAT_ABS.parse(endTime);
            if (st.before(et)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static long getDifferDate(String startTime, String endTime) {
        long hour = -1;
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        try {
            long diff;
            if (!TextUtils.isEmpty(endTime)) {
                diff = DATE_FORMAT_ABS.parse(endTime).getTime() - DATE_FORMAT_ABS.parse(startTime).getTime();
            } else {
                diff = DATE_FORMAT_ABS.parse(startTime).getTime() - (new Date().getTime());
            }
            hour = diff / nh;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hour;
    }

    /**
     * 获取年龄值
     *
     * @param birthday
     * @return
     */
    public static int getAge(String birthday) {
        if (TextUtils.isEmpty(birthday)) return 0;
        String yearStr = birthday.substring(0, 4);
        int integer = Integer.parseInt(yearStr);
        int newYear = new GregorianCalendar().get(Calendar.YEAR);
        return newYear - integer;
    }

    /**
     * 计算时间差
     *
     * @param time
     * @return
     */
    public static String getTimeDifferToString(long time) {
        String timeStr = "";
        long differTime = time - (new Date().getTime());
        int min = (int) (differTime / 60000);
        int hour = (min / 60);
        int day = (hour / 24);
        if (day > 0) {
            timeStr += day + "天";
        }
        if (hour > 0) {
            timeStr += hour - (24 * day) + "小时";
        }
        if (min > 0) {
            timeStr += min - (hour * 60) + "分钟";
        }
        return timeStr;
    }

    /**
     * 计算时间差
     *
     * @param differTime 单位：s
     * @return
     */
    public static String getTimeDiffer(long differTime) {
        String timeStr = "";
        int min = (int) (differTime / 60);
        int hour = (min / 60);
        int day = (hour / 24);
        if (day > 0) {
            timeStr += day + "天";
        }
        if (hour > 0) {
            timeStr += hour - (24 * day) + "小时";
        }
        if (min > 0) {
            timeStr += min - (hour * 60) + "分钟";
        }
        return timeStr;
    }


    /**
     * 计算两时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        if (day < 1) {
            if (hour < 1) {
                if (min < 0) {
                    return "已超时订单";
                }

                return min + "分钟";
            }
            return hour + "小时" + min + "分钟";
        }

        return day + "天" + hour + "小时" + min + "分钟";
    }


    /**
     * 根据当前年月获取天数
     */
    public static int getDaysByMonth(int year, int month) {
        int[] monDays = new int[]{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (((year) % 4 == 0 && (year) % 100 != 0) || (year) % 400 == 0)
            monDays[2]++;
        return monDays[month];
    }

    public static String timeStyle(long times) {
        String time = null;
        long timedata = System.currentTimeMillis();

        if (timedata - times < 60000) {
            time = "刚刚发布";
        } else if (timedata - times >= 60000 && timedata - times < 3600000) {
            float timege = (timedata - times) / 60000;
            time = Math.round(timege) + "分钟前发布";
        } else if (timedata - times >= 3600000 && timedata - times < 3600000 * 24) {
            float timege = (timedata - times) / 3600000;
            time = Math.round(timege) + "小时前发布";
        } else {
            time = timeLongToString(times);
        }
        return time;
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
     *
     * @param time
     * @return
     */
    public static String timeLongToString(long time) {
        Date date = new Date(time);
        String strs = "";
        try {
            //yyyy表示年MM表示月dd表示日
            //yyyy-MM-dd是日期的格式，比如2015-12-12如果你要得到2015年12月12日就换成yyyy年MM月dd日
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //进行格式化
            strs = sdf.format(date);
            System.out.println(strs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }


    /**
     * 是否为今天
     *
     * @param time
     * @return
     */
    public static boolean isToday(long time) {
        return getTimes(time).equals(getTimes(new Date().getTime()));
    }

    /**
     * 是否为今年
     */
    public static boolean isThisYear(long time) {
        return getTime(time, new SimpleDateFormat("yyyy")).equals(getTime(new Date().getTime(), new SimpleDateFormat("yyyy")));
    }
}
