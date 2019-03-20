package com.zjzf.shoescircle.ui.widget.pullrecyclerview.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.zjzf.shoescircle.lib.utils.LogHelper;


/**
 * Created by 陈志远 on 2017/6/1.
 */

public class RecyclerViewUtil {
    /**
     * 判断recyclerview是否滑到底部
     * <p>
     * 原理：判断滑过的距离加上屏幕上的显示的区域是否比整个控件高度高
     *
     * @return
     */
    public static boolean isScrollToBottom(@NonNull RecyclerView recyclerView) {
        return recyclerView != null && recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange();
    }

    public static boolean isOnTop(@NonNull RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) return false;
        if (layoutManager instanceof LinearLayoutManager) {
            int pos = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            View v = (layoutManager).findViewByPosition(pos);
            if (v != null && v.getTop() == 0 && pos == 0) {
                return true;
            }
            return ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition() == 0;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int aa[] = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null);
            View child0 = recyclerView.getChildAt(0);
            if (child0 == null) return false;
            int decoratedHeight = layoutManager.getTopDecorationHeight(recyclerView.getChildAt(0));
            LogHelper.trace("child 0 y  >>  " + child0.getY() + "  aa[0]  >>  " + aa[0] + "  decoratedHeight  >>  " + decoratedHeight);
            return recyclerView.getChildAt(0).getY() - decoratedHeight == 0f && aa[0] == 0;
        }
        return false;
    }
}
