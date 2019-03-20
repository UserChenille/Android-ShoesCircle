package com.zjzf.shoescircle.ui.widget.pullrecyclerview.interfaces;

import android.support.v7.widget.RecyclerView;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by 陈志远 on 2017/6/1.
 * <p>
 * 下拉刷新与加在更多
 */

public interface OnRefreshListener {

    void onRefreshing(PtrFrameLayout frame, RecyclerView recyclerView, int page, int rows);

    void onLoadMore(PtrFrameLayout frame, RecyclerView recyclerView, int page, int rows);
}
