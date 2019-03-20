package com.zjzf.shoescircleandroid.base.net.model;

/**
 * @author lneartao
 * @date 2018/7/3.
 */
public class ObjectData<T> extends BaseResultData {
    private T data;

    @Override
    public T getData() {
        return data;
    }
}
