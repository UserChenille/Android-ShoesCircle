package com.zjzf.shoescircle.lib.helper.rx.subscriber;



import com.zjzf.shoescircle.lib.utils.LogHelper;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by 陈志远 on 2018/5/24.
 */
public abstract class BaseSubscriber<T> extends DisposableObserver<T> {
    protected final String TAG = this.getClass().getSimpleName();

    public BaseSubscriber() {

    }

    @Override
    protected void onStart() {
        LogHelper.trace("Subscriber ->onStart()");
    }

    @Override
    public void onComplete() {
        LogHelper.trace("Subscriber ->onComplete()");
    }


    @Override
    public final void onError(Throwable e) {
        LogHelper.trace("Subscriber ->onError()");
        onSubScribeError(e);
    }

    public abstract void onSubScribeError(Throwable e);
}
