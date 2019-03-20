package com.zjzf.shoescircle.lib.interfaces;

/**
 * Created by 陈志远 on 2018/6/13.
 * 升级版simpleCallback
 */
public abstract class ExtSimpleCallback<T> implements SimpleCallback<T> {

    public void onStart() {
    }

    public void onError(int code,String errorMessage) {
    }

    public void onFinish() {
    }
}
