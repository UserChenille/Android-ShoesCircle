package com.zjzf.shoescircleandroid.base.net.model;

import java.util.List;

/**
 * @author lneartao
 * @date 2018/7/3.
 */
public class ListData<T> extends BaseResultData {

    private WrapList<T> data;

    @Override
    public WrapList<T> getData() {
        return data;
    }

    public List<T> getList() {
        return data == null ? null : data.getList();
    }
}
