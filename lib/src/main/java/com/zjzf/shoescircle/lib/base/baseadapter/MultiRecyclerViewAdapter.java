package com.zjzf.shoescircle.lib.base.baseadapter;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by 陈志远 on 2018/4/12.
 */
public class MultiRecyclerViewAdapter<T extends MultiType> extends BaseMultiRecyclerViewAdapter<MultiRecyclerViewAdapter, T> {
    public MultiRecyclerViewAdapter(@NonNull Context context) {
        super(context);
    }

    public MultiRecyclerViewAdapter(@NonNull Context context, @NonNull List<T> datas) {
        super(context, datas);
    }
}
