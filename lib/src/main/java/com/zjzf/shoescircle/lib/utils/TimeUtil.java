package com.zjzf.shoescircle.lib.utils;

import android.support.annotation.StringDef;
import android.text.TextUtils;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by 张智涛 on 2017/3/22.
 */

public class TimeUtil {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({YYYYMMDD, YYYYMM, HHMM, YYYYMMDDHHMMSS, YYYYMMDDHHMM, MMDDHHMM_CHINESE, MMDDHHMM_CHINESE_SHORT, YYYYMMDD_CHINESE, YYYYMMDD_SLASH, YYYYMM_SLASH, MMDD, YYYYMMDD_DOT})
    public @interface FormateType {
    }

    public final static String YYYYMMDD = "yyyy-MM-dd";
    public final static String YYYYMM = "yyyy-MM";
    public final static String HHMM = "HH:mm";
    public final static String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    public final static String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public final static String MMDDHHMM_CHINESE = "MM月dd日 HH:mm";
    public final static String MMDDHHMM_CHINESE_SHORT = "MM月dd日";
    public final static String YYYYMMDD_CHINESE = "yyyy年MM月dd日";
    public final static String YYYYMMDD_SLASH = "yyyy/MM/dd";
    public final static String YYYYMM_SLASH = "yyyy/MM";
    public final static String MMDD = "MM-dd";
    public final static String YYYYMMDD_DOT = "yyyy.MM.dd";

    public static final long YEAR = 365 * 24 * 60 * 60;// 年
    public static final long MONTH = 30 * 24 * 60 * 60;// 月
    public static final long DAY = 24 * 60 * 60;// 天
    public static final long HOUR = 60 * 60;// 小时
    public static final long MINUTE = 60 ;// 分钟
    public static final long SECOND = 1 ;// 秒

    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    public static String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    public static String getFetureDate(int past, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        String result = format.format(today);
        return result;
    }

    public static String getWhichDay(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        String result = format.format(today);
        return result;
    }

    public static String formatDate(String date, String dateFormat) throws ParseException {
        SimpleDateFormat inputSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = inputSdf.parse(date);
        SimpleDateFormat ouputSdf = new SimpleDateFormat(dateFormat);
        return ouputSdf.format(dateTime);
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param date 20170412
     * @return 04-12
     */
    public static String formatDate(String date) {
        if (!TextUtils.isEmpty(date) && date.length() == 8) {
            return new StringBuilder(date.substring(4)).insert(2, "-").toString();
        }
        return date;
    }

    /**
     * 时间戳转格式化日期
     *
     * @param timestamp 单位毫秒
     * @param format    日期格式
     * @return
     */
    public static String longToTimeStr(long timestamp, @FormateType String format) {
        return transferLongToDate(timestamp, format);
    }

    /**
     * 时间戳转格式化日期
     *
     * @param timestamp 单位毫秒
     * @param format    日期格式
     * @return
     */
    private static String transferLongToDate(long timestamp, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = new Date(timestamp);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 格式化日期转时间戳
     *
     * @param format 日期格式
     * @return
     */
    public static long strToTimestamp(String date, String format) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(format).parse(date).getTime() / 1000;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 比较timeTwo是否比TimeOne晚超过distanceMinute分钟
     *
     * @param TimeOne        单位 unixtime
     * @param timeTwo        单位 unixtime
     * @param distanceMinute 单位分钟
     * @return
     */
    public static boolean isGreaterThan(long TimeOne, long timeTwo, int distanceMinute) {
        long timeDifference = timeTwo - TimeOne;// 两者相距的秒数
        long oneMinute = 60;
        if (timeDifference > distanceMinute * oneMinute) {
            return true;
        } else {
            return false;
        }
    }

    public static String transferDateFromate(String originalDate, @FormateType String oldFormate, @FormateType String newFormate) {
        long time = stringToTimeStamp(originalDate, oldFormate);
        return transferDateFromate(time, newFormate);
    }

    public static String transferDateFromate(long originalDate, @FormateType String newFormate) {
        return longToTimeStr(originalDate, newFormate);
    }

    public static String transferDateFromate(String originalDate, @FormateType String newFormate) {
        return transferDateFromate(originalDate, getTimeFormatPatten(originalDate), newFormate);
    }

    public static String formatLongToTimeStr(Long l) {
        String str = "";
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue() / 1000;
        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }

        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }

        String strtime = "";
        if (hour != 0) {
            strtime = hour + ":" + minute + ":" + second;
        } else {
            strtime = minute + ":" + (second >= 10 ? second : "0" + second);
        }
        return strtime;
    }

    public static long stringToTimeStamp(String timeString) {
        String patten = getTimeFormatPatten(timeString);
        if (TextUtils.isEmpty(patten)) return 0;
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        try {
            return sdf.parse(timeString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long stringToTimeStamp(String timeString, @FormateType String format) {
        if (TextUtils.isEmpty(timeString) || TextUtils.isEmpty(format)) return 0;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(timeString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long stringToTimeStamp(String timeString, @FormateType String format, TimeZone timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        sdf.setTimeZone(timeZone);
        try {
            return sdf.parse(timeString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean isToday(long timeInMilis) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timeInMilis);
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.DATE) == time.get(Calendar.DATE)) {
            return true;
        } else {
            return false;
        }
    }

    public static int getSubDay(long start, long end) {
        int result = Math.round((end - start) / (1000 * DAY));
        return result < 0 ? -1 : result;
    }

    public static String getTimeFormatPatten(long time) {
        return TimeFormatChecker.getTimeFormatPatten(time);
    }

    public static String getTimeFormatPatten(String time) {
        return TimeFormatChecker.getTimeFormatPatten(time);
    }

    static class TimeFormatChecker {
        static SimpleDateFormat mCheckFormater = new SimpleDateFormat();
        static final String[] patten = {"yyyy-MM-dd", "yyyy-MM", "HH:mm",
                "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "MM月dd日 HH:mm",
                "MM月dd日", "yyyy年MM月dd日", "yyyy/MM/dd",
                "yyyy/MM", "MM-dd", "yyyy.MM.dd"};

        public static String getTimeFormatPatten(long time) {
            String result = "";
            for (String s : patten) {
                mCheckFormater.applyPattern(s);
                try {
                    result = mCheckFormater.format(time);
                    if (!TextUtils.isEmpty(result)) {
                        return s;
                    }
                } catch (Exception e) {
                }
            }
            return null;
        }

        public static String getTimeFormatPatten(String time) {
            long result = 0;
            for (String s : patten) {
                mCheckFormater.applyPattern(s);
                try {
                    result = mCheckFormater.parse(time).getTime();
                    if (result != 0) {
                        return s;
                    }
                } catch (Exception e) {
                }
            }
            return null;
        }


    }
}
