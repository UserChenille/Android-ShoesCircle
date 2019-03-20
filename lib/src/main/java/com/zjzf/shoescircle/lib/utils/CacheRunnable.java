package com.zjzf.shoescircle.lib.utils;

import android.os.Looper;

import com.zjzf.shoescircle.lib.other.WeakHandler;

/**
 * Created by 陈志远 on 2019/2/23.
 */
public class CacheRunnable {
    private static final long BLUR_TASK_WAIT_TIMEOUT = 1000;//图片模糊超时1秒
    Runnable action;
    long delay;
    private WeakHandler mWeakHandler = new WeakHandler(Looper.getMainLooper());

    public CacheRunnable(Runnable action) {
        this(action, 0);
    }

    public CacheRunnable(Runnable action, long delay) {
        this.action = action;
        this.delay = delay;
    }

    public void restore() {
        if (action != null) {
            mWeakHandler.post(action);
        }
    }


    public void destroy() {
        if (action != null) {
            mWeakHandler.removeCallbacks(action);
        }
        mWeakHandler = null;
        action = null;
        delay = 0;

    }


    public boolean matches(Runnable otherAction) {
        return otherAction == null && action == null
                || action != null && action.equals(otherAction);
    }
}
