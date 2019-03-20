package com.zjzf.shoescircleandroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;

import com.simple.spiderman.SpiderMan;
import com.tencent.bugly.crashreport.CrashReport;
import com.zjzf.shoescircle.lib.helper.AppFileHelper;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.ThirdLoginManager;
import com.zjzf.shoescircleandroid.base.manager.RongYunManager;
import com.zjzf.shoescircleandroid.base.manager.UserInfoManager;

import java.lang.ref.WeakReference;


/**
 * Created by 陈志远 on 2018/7/20.
 */
public class ShoesCircleApp extends MultiDexApplication {

    static ShoesCircleApp INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        AppFileHelper.init(this);
        ThirdLoginManager.INSTANCE.init(this);
        RongYunManager.INSTANCE.init(this);
        UserInfoManager.INSTANCE.init();
        registerActivityLifeCycleCallback();
        if (BuildConfig.DEBUG) {
            CrashReport.initCrashReport(getApplicationContext(), "705dc41af5", true);
            SpiderMan.init(this).setTheme(R.style.SpiderManTheme_Dark);
        } else {
            CrashReport.initCrashReport(getApplicationContext(), "705dc41af5", false);
        }
    }

    public static ShoesCircleApp getInstance() {
        return INSTANCE;
    }

    private WeakReference<Activity> mWeakActivity;

    private void registerActivityLifeCycleCallback() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                mWeakActivity = new WeakReference<>(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    @Nullable
    public Activity getTopActivity() {
        return mWeakActivity == null ? null : mWeakActivity.get();
    }

}
