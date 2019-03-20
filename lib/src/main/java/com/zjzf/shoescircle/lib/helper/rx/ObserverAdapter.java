package com.zjzf.shoescircle.lib.helper.rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class ObserverAdapter<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public abstract void onNext(T t);

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
