package com.zjzf.shoescircle.lib.base.baseadapter;

import android.view.View;

/**
 * Created by 陈志远 on 2017/4/27.
 */

public abstract class OnBaseRecyclerViewItemClickListener<T, V extends BaseRecyclerViewHolder<T>> {
    public void onItemClick(V holder, View v, int position, T data) {
        onItemClick(v, position, data);
    }

    public abstract void onItemClick(View v, int position, T data);
}
