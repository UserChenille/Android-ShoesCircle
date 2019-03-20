package com.zjzf.shoescircle.lib.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DecimalFormat;
import java.util.Locale;

public class NumberUtil {


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_PERCENT, TYPE_PERCENT_NO_PLUS, TYPE_MONEY})
    private @interface ShowType {
    }

    public static final int TYPE_PERCENT = 0;
    public static final int TYPE_PERCENT_NO_PLUS = 1;
    public static final int TYPE_MONEY = 2;



    public static String format(double value, @ShowType int showType) {
        return format(value, showType, DecimalUtil.dfMoney);
    }

    public static String format(double value, @ShowType int showType, DecimalFormat format) {
        switch (showType) {
            case TYPE_PERCENT:
                return formatNumber(value, format, true, true);
            case TYPE_PERCENT_NO_PLUS:
                return formatNumber(value, format, true, false);
            case TYPE_MONEY:
                return formatNumber(value, format, false, false);
            default:
                break;
        }
        return "";
    }

    private static String formatNumber(double value, DecimalFormat format, boolean withPercent, boolean withPlus) {
        StringBuilder rules = new StringBuilder();
        if (withPlus) {
            rules.append(value > 0 ? "+" : "");
        }
        rules.append("%1$s");
        if (withPercent) {
            rules.append("%%");
        }
        return String.format(Locale.getDefault(), rules.toString(), format == null ? "" : format.format(value));
    }
}
