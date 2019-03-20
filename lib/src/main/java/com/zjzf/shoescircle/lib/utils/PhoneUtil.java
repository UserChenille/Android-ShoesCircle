package com.zjzf.shoescircle.lib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 陈志远 on 2018/7/3.
 */
public class PhoneUtil {
    public static String encryPhone(String number) {
        if (!TextUtils.isEmpty(number)
                && number.length() > 10) {
            String textPrefix = number.substring(0, 3);
            String textSuffix = number.substring(number.length() - 4);
            return textPrefix + "****" + textSuffix;
        } else {
            return number;
        }
    }

    /**
     * 跳转到拨号页面
     */
    public static void diallPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 直接拨号
     */
    @SuppressLint("MissingPermission")
    public static void callPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        context.startActivity(intent);
    }

    public static boolean isPhoneNumber(final String str) {
        if (TextUtils.isEmpty(str)) return false;
        Matcher m;
        boolean b;

        if (str.length() > 9) {
            m = Pattern.compile("^[1][3-9][0-9]{9}$").matcher(str);
            b = m.matches();
        } else {
            m = Pattern.compile("^[1][3-9][0-9]{9}$").matcher(str);
            b = m.matches();
        }
        return b;
    }
}
