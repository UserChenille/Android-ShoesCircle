package com.zjzf.shoescircle.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 陈志远 on 2017/4/27.
 * <p>
 * 设置gridlayout间距的Decoration
 */

public class GridLayoutSpaceDecoration extends RecyclerView.ItemDecoration {
    private int left;
    private int right;
    private int top;
    private int bottom;

    private int headerPos = -1;


    public GridLayoutSpaceDecoration(int space) {
        this(space, -1);
    }

    public GridLayoutSpaceDecoration(int space, int headerPos) {
        this(space, space, space, space, headerPos);
    }


    public GridLayoutSpaceDecoration(int left, int top, int right, int bottom, int headerPos) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.headerPos = headerPos;
    }

    public int getHeaderPos() {
        return headerPos;
    }

    public void setHeaderPos(int headerPos) {
        this.headerPos = headerPos;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int position = parent.getChildAdapterPosition(view);
        if (headerPos > -1) {
            if (position < headerPos) {
                outRect.set(0, 0, 0, 0);
                return;
            }
        }
        outRect.set(left,top,right,bottom);
    }
}
