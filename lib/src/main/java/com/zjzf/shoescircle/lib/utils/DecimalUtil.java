package com.zjzf.shoescircle.lib.utils;

import android.content.Context;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by xxqiang on 15/12/3.
 */
public class DecimalUtil {
    /**etc. 实例num: 120000000000.15331313*/

    /**
     * etc. 120,000,000,000.15
     */
    public static DecimalFormat dfMoney = new DecimalFormat("#,##0.00");
    /**
     * etc. 120,000,000,000.15
     */
    public static DecimalFormat dfMoney2 = new DecimalFormat("#,##0.##");
    /**
     * etc. 120,000,000,000.1530
     */
    public static DecimalFormat dfMoney3 = new DecimalFormat("#,##0.0000");
    /**
     * etc. 120,000,000,000.15
     */
    public static DecimalFormat dfNHSYL = new DecimalFormat("#,##0.0#");
    /**
     * etc. 120000000000.15
     */
    public static DecimalFormat dfMoney4 = new DecimalFormat("0.00");
    //保留两位小数，如果是.0结尾，则忽略
    /**
     * etc. 120000000000.15
     */
    public static DecimalFormat dfRate = new DecimalFormat("#.##");
    /**
     * etc.  120,000,000,000
     */
    public static DecimalFormat dfMoney5 = new DecimalFormat("#,##0");


    /**
     * 千位分隔符,并且小数点后保留2位
     *
     * @param money
     * @return String
     */
    public static String format(double money) {
        return dfMoney.format(money);
//        return String.format("%.2f", money);
    }


    public static String formatNetValue(double money) {
        return dfMoney3.format(money);
    }

    /**
     * 保留4位小叔
     *
     * @param money
     * @return
     */
    public static String format4(double money) {
        return dfMoney3.format(money);
    }

    public static String format5(double money) {
        return dfMoney5.format(money);
    }

    public static String format2(double money) {
        return dfMoney4.format(money);
    }

    public static String formatYHQ(double money) {
        return dfMoney2.format(money);
    }

    /**
     * 最多保留两位小数，最少保留一位小数
     *
     * @param nhsyl
     * @return
     */
    public static String formatSYL(double nhsyl) {
        return dfNHSYL.format(nhsyl);
    }

    public static String formatNoQwf(double money) {
        DecimalFormat df = new DecimalFormat("###.00");
        return df.format(money);
    }

    public static String qianweifuInt(double num) {
        DecimalFormat df = new DecimalFormat("#,##0.##");
        return df.format(num);
    }

    public static boolean lower(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(d1);
        BigDecimal bd2 = new BigDecimal(d2);
        return bd1.compareTo(bd2) < 0;
    }

    public static boolean lowerOrequal(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(d1);
        BigDecimal bd2 = new BigDecimal(d2);
        return bd1.compareTo(bd2) <= 0;
    }

    public static boolean equal(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(d1);
        BigDecimal bd2 = new BigDecimal(d2);
        return bd1.equals(bd2);
    }

    public static boolean greaterOrEqual(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(d1);
        BigDecimal bd2 = new BigDecimal(d2);
        return bd1.compareTo(bd2) >= 0;
    }

    public static boolean greater(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(d1);
        BigDecimal bd2 = new BigDecimal(d2);
        return bd1.compareTo(bd2) > 0;
    }

    public static String formatRateNum(double num) {
        return dfRate.format(num);
    }

    public static String formatAutoRateNum(double num) {
        return new DecimalFormat("#.0").format(num);
    }

    public static String formatMoney(Context context, double money) {
        if (greaterOrEqual(Math.abs(money), 10000)) {
            return new DecimalFormat("#.#").format(money / 10000) + "万";
        } else {
            return new DecimalFormat("#.#").format(money);
        }
    }

    public static String formatYiMoney(double money) {
        double yi = 100000000;
        if (greaterOrEqual(Math.abs(money), yi)) {
            return format(money / yi) + "亿";
        } else {
            return format(money);
        }
    }


    //-----------------------------------------new -----------------------------------------

}
