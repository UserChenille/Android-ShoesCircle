package com.zjzf.shoescircle.lib.utils;

import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.base.AppContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by xxqiang on 16/8/26.
 */

public class StringUtil {
    /**
     * 删除Html标签
     *
     * @param inputString
     * @return
     */
    public static String htmlRemoveTag(String inputString) {
        if (inputString == null)
            return null;
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        try {
            //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            String regEx_html = "<[^>]+>|&nbsp;"; // 定义HTML标签或空格的正则表达式
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textStr;// 返回文本字符串
    }

    public static boolean noEmpty(CharSequence source) {
        return !TextUtils.isEmpty(source);
    }

    public static boolean noEmpty(CharSequence... source) {
        for (CharSequence charSequence : source) {
            if (TextUtils.isEmpty(charSequence)) return false;
        }
        return true;
    }


    public static boolean isEmptyWithNull(String source) {
        return source == null || TextUtils.isEmpty(source.trim()) || TextUtils.equals(source, "null") || TextUtils.equals(source, "Null") || TextUtils.equals(source, "NULL");
    }

    public static String trim(String source) {
        if (noEmpty(source)) {
            return source.trim();
        }
        return source;

    }

    public static CharSequence trim(CharSequence cs) {
        int len = cs.length();
        int st = 0;

        while ((st < len) && (cs.charAt(st) <= ' ')) {
            st++;
        }
        while ((st < len) && (cs.charAt(len - 1) <= ' ')) {
            len--;
        }
        return ((st > 0) || (len < cs.length())) ? cs.subSequence(st, len) : cs;
    }


    public static int cleanNumber(String fundRiskLevel) {
        switch (fundRiskLevel) {
            case "0":
                return 0;
            case "1":
            case "01":
                return 1;
            case "2":
            case "02":
                return 2;
            case "3":
            case "03":
                return 3;
            case "4":
            case "04":
                return 4;
            case "5":
            case "05":
                return 5;
            case "6":
            case "06":
                return 6;
            case "7":
            case "07":
                return 7;
            case "8":
            case "08":
                return 8;
            case "9":
            case "09":
                return 9;
            default:
                return 0;

        }

    }

    public static String formatNumberPre(String num) {
        return formatNumberPre(cleanNumber(num));
    }

    public static String formatNumberPre(int num) {
        if (num >= 0 && num < 10) {
            return "0" + String.valueOf(num);
        }
        return String.valueOf(num);
    }

    /**
     * 从资源文件得到文字并format
     */
    public static String getString(@StringRes int strId, Object... objs) {
        if (strId == 0) return null;
        return AppContext.getAppContext().getResources().getString(strId, objs);
    }

    public static void setHtmlText(TextView textView, @StringRes int stringResId, Object... objs) {
        setHtmlText(textView, getString(stringResId, objs));
    }

    public static void setHtmlText(TextView textView, String str) {
        if (textView == null) return;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(str));
        }
    }

    public static String formatPercent(double value, boolean withPercent) {
        return formatPercent(value, DecimalUtil.dfMoney, withPercent);
    }

    public static String formatPercent(double value, DecimalFormat format, boolean withPercent) {
        String rules;
        if (value > 0) {
            rules = "+%1$s" + (withPercent ? "%%" : "");
        } else {
            rules = "%1$s" + (withPercent ? "%%" : "");
        }
        return String.format(Locale.getDefault(), rules, format == null ? "" : format.format(value));
    }

    public static int compareNumber(String n1, String n2) {

        String[] v1 = n1.split("\\.");
        String[] v2 = n2.split("\\.");

        for (int i = 0; i < Math.max(v1.length, v2.length); i++) {
            int num1 = i < v1.length ? Integer.parseInt(v1[i]) : 0;
            int num2 = i < v2.length ? Integer.parseInt(v2[i]) : 0;
            if (num1 < num2) {
                return -1;
            } else if (num1 > num2) {
                return 1;
            }
        }

        return 0;
    }

    public static boolean checkPwdCorrect(String psw) {
        String wordRegex = "[a-zA-Z]";
        String specialRegex = "[~!@#$%\\\\^&*\\(\\)_+`\\-=]";
        String numberRegex = "\\d+";
        String regex =
                "(((?=.*" + wordRegex + ")(?=.*" + specialRegex + "))|((?=.*" + wordRegex + ")(?=.*" + numberRegex + "))|" +
                        "((?=.*" + numberRegex + ")(?=.*" + specialRegex + "))"
                        + ")";
        ///^\\(?!^(\\d+|[a-zA-Z]+|[~!@#$%\\^&*\\(\\)_+`\\-=]+)$)^[\\w~!@#$%\\^&*\\(\\)+`\\-=]{6,16}$/;

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(psw);
        return matcher.find();
    }

    public static String getEncryPhone(String number) {
        if (!TextUtils.isEmpty(number)
                && number.length() > 10) {
            String textPrefix = number.substring(0, 3);
            String textSuffix = number.substring(number.length() - 4);
            return textPrefix + "****" + textSuffix;
        } else {
            return number;
        }
    }

    public static double toDouble(String value) {
        try {
            BigDecimal b = new BigDecimal(value);
            return b.doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long toLong(String value) {
        try {
            BigDecimal b = new BigDecimal(value);
            return b.longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int toInt(String value) {
        try {
            BigDecimal b = new BigDecimal(value);
            return b.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static double toFloat(String value) {
        try {
            BigDecimal b = new BigDecimal(value);
            return b.floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    /**
     * 判断是不是浮点型数据
     *
     * @param str
     * @return
     */
    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_MIDDLE, TYPE_END, TYPE_START})
    private @interface EllipsizeType {
    }

    public static final int TYPE_MIDDLE = 1;
    public static final int TYPE_END = 2;
    public static final int TYPE_START = 0;

    /**
     * 省略字符串
     *
     * @param text
     * @param showType
     * @param showTextNum
     * @return
     */
    public static String ellipsizeText(String text, @EllipsizeType int showType, int showTextNum) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        if (text.length() <= showTextNum) {
            return text;
        }
        switch (showType) {
            case TYPE_START:
                return text.substring(text.length() - showTextNum) + "...";
            case TYPE_MIDDLE:
                int middlePosition = showTextNum / 2;
                return text.subSequence(0, middlePosition) + "..." + text.substring(text.length() - (showTextNum - middlePosition));
            case TYPE_END:
                return text.substring(0, showTextNum) + "...";
            default:
                return text;
        }

    }

    static String[] units = { "", "十", "百", "千", "万", "十万", "百万", "千万", "亿",
            "十亿", "百亿", "千亿", "万亿" };
    static char[] numArray = { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九' };

    public static String formatInteger(int num) {
        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = val[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero) {
                if ('0' == val[i - 1]) {
                    continue;
                } else {
                    sb.append(numArray[n]);
                }
            } else {
                sb.append(numArray[n]);
                sb.append(unit);
            }
        }
        return sb.toString();
    }


    public static String formatDecimal(double decimal) {
        String decimals = String.valueOf(decimal);
        int decIndex = decimals.indexOf(".");
        int integ = Integer.valueOf(decimals.substring(0, decIndex));
        int dec = Integer.valueOf(decimals.substring(decIndex + 1));
        String result = formatInteger(integ) + "." + formatFractionalPart(dec);
        return result;
    }


    public static String formatFractionalPart(int decimal) {
        char[] val = String.valueOf(decimal).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int n = Integer.valueOf(val[i] + "");
            sb.append(numArray[n]);
        }
        return sb.toString();
    }


}
