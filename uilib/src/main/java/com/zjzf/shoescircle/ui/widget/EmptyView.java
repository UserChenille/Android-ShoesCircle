package com.zjzf.shoescircle.ui.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zjzf.shoescircle.uilib.R;


/**
 * Created by 陈志远 on 2017/5/24.
 * <p>
 * 通用型emptyview
 */

public class EmptyView extends FrameLayout {

    private TextView emptyTextView;

    private OnEmptyClickListener mOnEmptyClickListener;

    public EmptyView(@NonNull Context context) {
        this(context, null);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        if (getId() == View.NO_ID) {
            setId(R.id.py_empty_view);
        }
        setVisibility(GONE);
        View.inflate(context, R.layout.widget_empty_view, this);
        emptyTextView = (TextView) findViewById(R.id.tv_common_empty);
    }

    public void setEmptyText(@StringRes int resid) {
        setEmptyText(getResources().getString(resid));
    }

    public void setEmptyText(CharSequence text) {
        if (emptyTextView != null) {
            emptyTextView.setText(text);
        }
    }

    public OnEmptyClickListener getOnEmptyClickListener() {
        return mOnEmptyClickListener;
    }

    public void setOnEmptyClickListener(OnEmptyClickListener onEmptyClickListener) {
        mOnEmptyClickListener = onEmptyClickListener;
        setOnClickListener(mOnEmptyClickListener);
    }

    public interface OnEmptyClickListener extends OnClickListener {

    }

}
