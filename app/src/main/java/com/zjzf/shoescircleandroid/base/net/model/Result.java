package com.zjzf.shoescircleandroid.base.net.model;

public interface Result <T>{
    String getMessage();

    int getErrorCode();

    long getTimeStamep();

    T getData();
}
