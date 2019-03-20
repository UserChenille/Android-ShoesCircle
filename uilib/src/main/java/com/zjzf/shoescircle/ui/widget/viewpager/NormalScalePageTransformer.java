package com.zjzf.shoescircle.ui.widget.viewpager;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * viewpager Y移动
 */
public class NormalScalePageTransformer implements ViewPager.PageTransformer {

    public void transformPage(View view, float position) {
        if (position < -1) {
            view.setAlpha(1);
            view.setRotationY(0);
        } else if (position <= 1) {
            if (position < 0) {
                view.setAlpha(1 - Math.abs(position));
                view.setRotationY(position * 45);
            } else {
                view.setAlpha(1 - Math.abs(position));
                view.setRotationY(position * 45);
            }
        } else {
            view.setAlpha(1);
            view.setRotationY(0);
        }
    }
}
