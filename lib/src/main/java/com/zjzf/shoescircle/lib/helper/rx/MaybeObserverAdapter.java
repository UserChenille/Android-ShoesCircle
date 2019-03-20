package com.zjzf.shoescircle.lib.helper.rx;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

public abstract class MaybeObserverAdapter<T> implements MaybeObserver<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public abstract void onSuccess(T t);

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
