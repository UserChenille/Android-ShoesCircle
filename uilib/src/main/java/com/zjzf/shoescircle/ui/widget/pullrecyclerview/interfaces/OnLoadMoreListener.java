package com.zjzf.shoescircle.ui.widget.pullrecyclerview.interfaces;

import android.support.v7.widget.RecyclerView;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by 陈志远 on 2017/6/1.
 * <p>
 * 加载更多接口
 */

public abstract class OnLoadMoreListener implements OnRefreshListener {
    /**
     * 当使用onLoadMore时，onRefresh不会被监听
     *
     * @param frame
     * @param recyclerView
     */
    @Deprecated
    @Override
    public void onRefreshing(PtrFrameLayout frame, RecyclerView recyclerView, int page, int rows) {

    }
}
