package com.zjzf.shoescircleandroid.widget.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.helper.AppFileHelper;
import com.zjzf.shoescircle.lib.utils.EventBusUtil;
import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircle.ui.widget.CircleProgressView;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.download.DownloadService;
import com.zjzf.shoescircleandroid.model.UpdateInfo;
import com.zjzf.shoescircleandroid.model.event.DownloadEvent;

import org.greenrobot.eventbus.Subscribe;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 陈志远 on 2017/7/19.
 */

public class PopupUpdateProgress extends BasePopupWindow {
    private static final String TAG = "PopupUpdateProgress";
    private CircleProgressView mCircleProgressView;
    private TextView content;
    private String downloadUrl;

    public PopupUpdateProgress(Context context) {
        super(context);
        setBlurBackgroundEnable(true);
        EventBusUtil.register(this);
        setBlurBackgroundEnable(true);
        setAllowDismissWhenTouchOutside(false);
        setBackPressEnable(false);
        mCircleProgressView = (CircleProgressView) findViewById(R.id.progress);
        content = (TextView) findViewById(R.id.content);
    }


    public void showPopupWindow(UpdateInfo updateInfo) {
        downloadUrl = updateInfo.getInstallUrl();
        String name = updateInfo.getName() + "_" + updateInfo.getVersionShort().replace(".", "_") + ".apk";
        mCircleProgressView.reset();
        DownloadService.download(getContext(), downloadUrl, AppFileHelper.getDownloadDir() + name);
        super.showPopupWindow();
    }

    @Override
    public void dismiss() {
        EventBusUtil.unregister(this);
        super.dismiss();
    }


    @Override
    protected Animator onCreateShowAnimator() {
        AnimatorSet set;
        set = new AnimatorSet();
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(getContentView(), View.TRANSLATION_Y, 300, 0).setDuration(600);
        transAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(getContentView(), View.ALPHA, 0.4f, 1).setDuration(250 * 3 / 2);
        set.playTogether(transAnimator, alphaAnimator);
        return set;
    }

    @Override
    protected Animator onCreateDismissAnimator() {
        AnimatorSet set;
        set = new AnimatorSet();
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(getContentView(), View.TRANSLATION_Y, 0, 250).setDuration(600);
        transAnimator.setInterpolator(new OvershootInterpolator(-6));
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(getContentView(), View.ALPHA, 1f, 0).setDuration(800);
        set.playTogether(transAnimator, alphaAnimator);
        return set;
    }


    @Subscribe
    public void onEvent(DownloadEvent event) {
        String url = event.getUrl();
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(downloadUrl)
                && url.equals(downloadUrl)) {
            updateState(event.getState(), event.getProgress());
        }
    }

    private void updateState(int status, int progress) {
        LogHelper.trace(TAG, "status=" + status + ",progress=" + progress);
        if (status == DownloadManager.STATUS_FAILED || status == -1) {
            dismiss();
        } else {
            mCircleProgressView.setCurrentPresent(progress);
        }

        switch (status) {
            case DownloadManager.STATUS_PENDING:
                content.setText("正在连接中...");
                break;
            case DownloadManager.STATUS_PAUSED:
                content.setText("已暂停（" + progress + "/100）");
                break;
            case DownloadManager.STATUS_RUNNING:
                content.setText("下载中（" + progress + "/100）");
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                content.setText("下载完成");
                dismiss();
                break;
            case DownloadManager.STATUS_FAILED:
                content.setText("下载失败");
                dismiss();
                break;
        }
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_progress);
    }
}
