package com.zjzf.shoescircle.lib.helper.rx.subscriber;


import com.zjzf.shoescircle.lib.utils.LogHelper;

import io.reactivex.functions.Consumer;

/**
 * Created by 陈志远 on 2018/5/24.
 * 默认打印日志的错误consumer
 */
public final class DefaultLogThrowableConsumer implements Consumer<Throwable> {
    private String TAG = this.getClass().getSimpleName();

    public DefaultLogThrowableConsumer() {
    }

    public DefaultLogThrowableConsumer(String TAG) {
        this.TAG = TAG;
    }

    @Override
    public void accept(Throwable throwable) throws Exception {
        LogHelper.trace(LogHelper.e,throwable);
    }
}
