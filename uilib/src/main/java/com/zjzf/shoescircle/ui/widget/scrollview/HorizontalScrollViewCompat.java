package com.zjzf.shoescircle.ui.widget.scrollview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by 陈志远 on 2017/11/7.
 */

public class HorizontalScrollViewCompat extends HorizontalScrollView {
    protected OnScrollChangeListenerCompat mOnScrollChangeListener;

    public HorizontalScrollViewCompat(Context context) {
        super(context);
    }

    public HorizontalScrollViewCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScrollViewCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HorizontalScrollViewCompat(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public OnScrollChangeListenerCompat getOnScrollChangeListener() {
        return mOnScrollChangeListener;
    }

    public void setScrollChangeListener(OnScrollChangeListenerCompat onScrollChangeListener) {
        mOnScrollChangeListener = onScrollChangeListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }
}
