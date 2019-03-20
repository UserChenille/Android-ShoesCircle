package com.zjzf.shoescircle.ui.widget.span;

import android.view.View;

import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.uilib.R;


/**
 * Created by 陈志远 on 2017/12/4.
 */
public class ClickSpan extends ClickableSpanEx {

    private View.OnClickListener mOnClickListener;

    public ClickSpan(View.OnClickListener onClickListener) {
        super(-1, UIHelper.getColor(R.color.common_blue), false);
        mOnClickListener = onClickListener;
    }

    @Override
    public void onClick(View widget) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(widget);
        }
    }
}
