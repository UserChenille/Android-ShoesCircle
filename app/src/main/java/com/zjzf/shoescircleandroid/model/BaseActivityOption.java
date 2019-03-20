package com.zjzf.shoescircleandroid.model;

import java.io.Serializable;

/**
 * Created by 陈志远 on 2018/8/5.
 */
public class BaseActivityOption<T extends BaseActivityOption> implements Serializable {
    int requestCode = -1;

    public int getRequestCode() {
        return requestCode;
    }

    public T setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return (T) this;
    }
}
