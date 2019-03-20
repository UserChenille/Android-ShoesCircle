package com.zjzf.shoescircle.lib.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by xxqiang on 15/11/20.
 */
public class AssetsUtil {
    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            if (in != null) {
                InputStreamReader inputreader = new InputStreamReader(in);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    result += line + "\n";
                }
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
