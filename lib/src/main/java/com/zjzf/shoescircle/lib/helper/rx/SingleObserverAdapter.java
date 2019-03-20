package com.zjzf.shoescircle.lib.helper.rx;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public abstract class SingleObserverAdapter<T> implements SingleObserver<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }
    @Override
    public abstract void onSuccess(T t);

    @Override
    public void onError(Throwable e) {

    }
}
