package com.zjzf.shoescircle.ui.widget.popup;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.zjzf.shoescircle.ui.widget.CircleProgressView;
import com.zjzf.shoescircle.uilib.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 陈志远 on 2018/4/27.
 * <p>
 * 进度条popup
 */

public class PopupProgress extends BasePopupWindow {

    private CircleProgressView progressView;
    private TextView progressTips;

    public PopupProgress(Activity context) {
        super(context);
        progressView = (CircleProgressView) findViewById(R.id.popup_progress);
        progressTips = (TextView) findViewById(R.id.progress_tips);
        progressView.setCurrentPresent(0);
        setBackPressEnable(false);
        setBlurBackgroundEnable(true);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultAlphaAnimation();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultAlphaAnimation(false);
    }


    public void setProgress(int progress) {
        if (progressView != null) {
            progressView.setCurrentPresent(progress);
        }
    }

    public void setProgressTips(String tips) {
        if (progressTips != null) {
            progressTips.setVisibility(TextUtils.isEmpty(tips) ? View.GONE : View.VISIBLE);
            progressTips.setText(tips);
        }
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_progress);
    }
}
