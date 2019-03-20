package com.zjzf.shoescircle.ui.widget.viewpager;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by 陈志远 on 2018/3/21.
 */
public class AlphaPageTransformer implements ViewPager.PageTransformer {

    private static final float DEFAULT_MIN_ALPHA = 0.5f;
    private float mMinAlpha = DEFAULT_MIN_ALPHA;

    public AlphaPageTransformer() {
    }

    public void transformPage(View view, float position) {
        if (position <= -1.0F || position >= 1.0F) {
            view.setAlpha(0.0F);
        } else if (position == 0.0F) {
            view.setAlpha(1.0F);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            view.setAlpha(1.0F - Math.abs(position));
        }
    }

}
