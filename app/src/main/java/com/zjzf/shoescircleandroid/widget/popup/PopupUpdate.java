package com.zjzf.shoescircleandroid.widget.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.base.AppContext;
import com.zjzf.shoescircle.lib.helper.AppFileHelper;
import com.zjzf.shoescircle.lib.helper.PermissionHelper;
import com.zjzf.shoescircle.lib.interfaces.IPermission;
import com.zjzf.shoescircle.lib.interfaces.OnPermissionGrantListener;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.model.UpdateInfo;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 陈志远 on 2017/7/19.
 */

public class PopupUpdate extends BasePopupWindow {

    private TextView title;
    private TextView content;
    private Button update;

    private UpdateInfo mUpdateInfo;

    public PopupUpdate(final Activity context) {
        super(context);
        setBlurBackgroundEnable(true);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        update = (Button) findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionAndUpdate(mUpdateInfo);
            }
        });

        findViewById(R.id.update_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUpdateInfo != null) {
                    Uri webPage = Uri.parse(mUpdateInfo.getInstallUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                        getContext().startActivity(intent);
                    }
                }
            }
        });
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void requestPermissionAndUpdate(UpdateInfo updateInfo) {
        if (getContext() instanceof IPermission) {
            ((IPermission) getContext()).getPermissionHelper().requestPermission(new OnPermissionGrantListener.OnPermissionGrantListenerAdapter() {
                @Override
                public void onPermissionGranted(PermissionHelper.Permission... grantedPermissions) {
                    if (mUpdateInfo != null) {
                        AppFileHelper.init(AppContext.getAppInstance());
                        new PopupUpdateProgress(getContext()).showPopupWindow(mUpdateInfo);
                        dismiss();
                    }
                }
            }, PermissionHelper.Permission.WRITE_EXTERNAL_STORAGE, PermissionHelper.Permission.READ_EXTERNAL_STORAGE);
        }
    }


    public void showPopupWindow(UpdateInfo updateInfo) {
        if (updateInfo == null) return;
        this.mUpdateInfo = updateInfo;
        bindData();
        super.showPopupWindow();
    }

    private void bindData() {
        title.setText(String.format("%s(%s)", mUpdateInfo.getName(), "v" + mUpdateInfo.getVersionShort()));
        content.setText(mUpdateInfo.getChangelog());
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


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_update);
    }
}
