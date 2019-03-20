package com.zjzf.shoescircle.lib.net.retrofit;

import android.support.annotation.Nullable;

public class OutsideLifecycleException extends IllegalStateException {

    public OutsideLifecycleException(@Nullable String detailMessage) {
        super(detailMessage);
    }
}
