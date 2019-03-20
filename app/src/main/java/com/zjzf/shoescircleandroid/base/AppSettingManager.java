package com.zjzf.shoescircleandroid.base;

import android.content.Context;
import android.content.SharedPreferences;

import com.zjzf.shoescircle.lib.base.AppContext;

import java.util.Map;

/**
 * Created by 陈志远 on 2017/5/9.
 * <p>
 * sharepreference类
 */
public class AppSettingManager {

    private static final String PREFERENCE_NAME = "shoescircle";
    private static SharedPreferences sharedPreferences;

    public static final String KEY_USER = "user";
    public static final String SEARCH_HISTORY = "searchHistory";

    static {
        sharedPreferences = AppContext.getAppContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static String loadString(String key, String defaultValue) {
        createSharedPreferencesIfNotExist();
        return sharedPreferences.getString(key, defaultValue);
    }

    public static boolean loadBoolean(String key, boolean defaultValue) {
        createSharedPreferencesIfNotExist();
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static float loadFloat(String key, float defaultValue) {
        createSharedPreferencesIfNotExist();
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static long loadLong(String key, long defaultValue) {
        createSharedPreferencesIfNotExist();
        return sharedPreferences.getLong(key, defaultValue);
    }

    public static Map<String, ?> loadAll() {
        createSharedPreferencesIfNotExist();
        return sharedPreferences.getAll();
    }

    public static int loadInteger(String key, int defaultValue) {
        createSharedPreferencesIfNotExist();
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void saveString(String key, String value) {
        createSharedPreferencesIfNotExist();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void saveBoolean(String key, boolean value) {
        createSharedPreferencesIfNotExist();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void saveInteger(String key, int value) {
        createSharedPreferencesIfNotExist();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void saveFloat(String key, float value) {
        createSharedPreferencesIfNotExist();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static void saveLong(String key, long value) {
        createSharedPreferencesIfNotExist();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void remove(String key) {
        createSharedPreferencesIfNotExist();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void clearAllPreference() {
        createSharedPreferencesIfNotExist();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }

    private static void createSharedPreferencesIfNotExist() {
        if (sharedPreferences == null) {
            sharedPreferences = AppContext.getAppContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
    }

}
