package com.zjzf.shoescircle.ui.widget.pullrecyclerview.footer;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.zjzf.shoescircle.uilib.R;


/**
 * Created by 陈志远 on 2017/6/1.
 * <p>
 * footer
 */

public class PtrFooter extends FrameLayout {
    public PtrFooter(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public PtrFooter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PtrFooter(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    void initView(Context context) {
        View.inflate(context, R.layout.layout_load_more, this);
    }
}
