package com.zjzf.shoescircle.ui.widget.pullrecyclerview.interfaces;

import android.support.v7.widget.RecyclerView;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by 陈志远 on 2017/6/1.
 * <p>
 * 下拉刷新接口
 */

public abstract class OnPullRefreshListener implements OnRefreshListener {
    /**
     * 使用onPullRefresh时，loadmore不会被监听
     *
     * @param frame
     * @param recyclerView
     */
    @Deprecated
    @Override
    public void onLoadMore(PtrFrameLayout frame, RecyclerView recyclerView, int page, int rows) {

    }
}
