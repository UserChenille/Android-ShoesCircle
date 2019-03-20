package com.zjzf.shoescircle.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.zjzf.shoescircle.ui.widget.textview.ExTextView;


/**
 * Created by 张智涛 on 2017/2/22.
 */

public class MarqueeTextView extends ExTextView {
    public MarqueeTextView(Context context) {
        super(context);
        init(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setMaxLines(1);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
