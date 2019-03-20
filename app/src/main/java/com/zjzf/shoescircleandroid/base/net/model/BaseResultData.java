package com.zjzf.shoescircleandroid.base.net.model;

/**
 * data有可能是T，有可能是WrapList<T>
 */
public abstract class BaseResultData<T> implements Result<T> {
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    private int code;
    private String msg;
    private long timestamp;


    @Override
    public long getTimeStamep() {
        return timestamp;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public int getErrorCode() {
        return code;
    }
}
