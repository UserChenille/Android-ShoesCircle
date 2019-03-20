package com.zjzf.shoescircleandroid.ui.me.fragments;

import android.support.v7.widget.RecyclerView;

import com.zjzf.shoescircle.ui.widget.pullrecyclerview.PullRecyclerView;
import com.zjzf.shoescircle.ui.widget.pullrecyclerview.interfaces.OnRefreshListener;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.fragment.BaseFragment;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by 陈志远 on 2018/11/24.
 */
public abstract class BaseMyGoodsFragment extends BaseFragment {
    @BindView(R.id.rv_content)
    protected PullRecyclerView mRvContent;

    private boolean hasInit;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_goods_demand;
    }

    @Override
    protected void initViews() {
        mRvContent.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefreshing(PtrFrameLayout frame, RecyclerView recyclerView, int page, int rows) {
                BaseMyGoodsFragment.this.onRefresh(page, rows);
            }

            @Override
            public void onLoadMore(PtrFrameLayout frame, RecyclerView recyclerView, int page, int rows) {
                BaseMyGoodsFragment.this.onLoadMore(page, rows);
            }
        });
        onInitRecyclerView(mRvContent.getRecyclerView());
        mRvContent.setAdapter(onInitAdapter());
        if (!hasInit) {
            mRvContent.manualRefresh();
            hasInit = true;
        }
    }

    protected void onInitRecyclerView(RecyclerView recyclerView) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !hasInit) {
            if (mRvContent != null) {
                mRvContent.manualRefresh();
                hasInit = true;
            }
        }
    }

    @Override
    protected void loadNetData() {

    }

    public PullRecyclerView getRvContent() {
        return mRvContent;
    }

    protected abstract RecyclerView.Adapter onInitAdapter();

    protected abstract void onRefresh(int page, int rows);

    protected abstract void onLoadMore(int page, int rows);

    public abstract String getTitle();

    public void compelete() {
        mRvContent.compelete();
    }

}
