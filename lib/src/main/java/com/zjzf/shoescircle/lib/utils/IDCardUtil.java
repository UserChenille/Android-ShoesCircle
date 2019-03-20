package com.zjzf.shoescircle.lib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 陈志远 on 2017/7/20.
 * <p>
 * 身份证工具类
 */

public class IDCardUtil {

    /**
     * 根据所传身份证号解析其性别
     */
    public static String parseGender(int sex) {
        return sex == 0 ? "女" : "男";
    }

    /**
     * 根据所传身份证号解析其性别
     */
    public static String parseGender(String cid) {
        int sex = parseGenderInteger(cid);
        return parseGender(sex);
    }

    /**
     * 根据所传身份证号解析其性别
     * 0:女
     * 1：男
     */
    public static int parseGenderInteger(String cid) {
        if (cid.length() < 2) return -1;
        int gender = 0;
        int sex = gender;
        char c = cid.charAt(cid.length() - 2);
        try {
            sex = Integer.parseInt(String.valueOf(c));
        } catch (Exception e) {
            sex = 0;
        }
        if (sex % 2 == 0) {
            gender = 0;
        } else {
            gender = 1;
        }
        return gender;
    }

    /**
     * 根据身份证号码，返回年龄
     */
    public static int parseAge(String cid) {
        if (cid.length() < 14) return -1;
        int age = 0;
        String birthDayStr = cid.substring(6, 14);
        Date birthDay = null;
        try {
            birthDay = new SimpleDateFormat("yyyyMMdd").parse(birthDayStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            return 0;
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayBirth = cal.get(Calendar.DAY_OF_MONTH);
        age = yearNow - yearBirth;
        if (monthNow < monthBirth ||
                (monthBirth == monthNow && dayNow < dayBirth)) {
            age--;
        }
        return age;
    }

    /**
     * 根据身份编号获取生日
     *
     * @param idCard 身份编号
     * @return 生日(yyyyMMdd)
     */
    public static String getBirthDayYYYYMMDD(String idCard) {
        StringBuilder builder = new StringBuilder();
        builder.append(getBirthYear(idCard))
                .append("-")
                .append(StringUtil.formatNumberPre(getBirthMonth(idCard)))
                .append("-")
                .append(StringUtil.formatNumberPre(getBirthDay(idCard)));

        return builder.toString();
    }

    /**
     * 根据身份编号获取生日年
     *
     * @param idCard 身份编号
     * @return 生日(yyyy)
     */
    public static Short getBirthYear(String idCard) {
        return Short.valueOf(idCard.substring(6, 10));
    }

    /**
     * 根据身份编号获取生日月
     *
     * @param idCard 身份编号
     * @return 生日(MM)
     */
    public static Short getBirthMonth(String idCard) {
        return Short.valueOf(idCard.substring(10, 12));
    }

    /**
     * 根据身份编号获取生日天
     *
     * @param idCard 身份编号
     * @return 生日(dd)
     */
    public static Short getBirthDay(String idCard) {
        return Short.valueOf(idCard.substring(12, 14));
    }
}
