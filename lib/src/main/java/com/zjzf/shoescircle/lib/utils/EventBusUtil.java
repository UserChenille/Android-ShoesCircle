package com.zjzf.shoescircle.lib.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xxqiang on 16/3/29.
 */
public class EventBusUtil {
    public static void register(Object object) {
        try {
            if (!EventBus.getDefault().isRegistered(object)) {
                EventBus.getDefault().register(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregister(Object object) {
        try {
            if (EventBus.getDefault().isRegistered(object)) {
                EventBus.getDefault().unregister(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void post(Object event) {
        try {
            EventBus.getDefault().post(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
