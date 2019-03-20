package com.zjzf.shoescircleandroid.ui.splash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.zjzf.shoescircle.lib.helper.RxHelper;
import com.zjzf.shoescircle.lib.helper.rx.base.RxCall;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.manager.UserInfoManager;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;

import java.util.logging.Logger;

import butterknife.BindView;

/**
 * Created by 陈志远 on 2018/7/22.
 */
public class SplashActivity extends BaseActivity {

    private static final long TIME_DELAY = 2 * 1000;
    @BindView(R.id.iv_bg)
    ImageView mIvBg;
    @BindView(R.id.iv_logo)
    ImageView mIvLogo;

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onInitView(View decorView) {
        RxHelper.delay(TIME_DELAY, new RxCall<Long>() {
            @Override
            public void onCall(Long data) {
                checkAndEnterNextActivity();
            }
        });

    }

    private void checkAndEnterNextActivity() {
        AnimatorSet set = new AnimatorSet();
        ValueAnimator alpha = ValueAnimator.ofFloat(1f, 0);
        alpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mIvBg.setAlpha((Float) animation.getAnimatedValue());
                mIvLogo.setAlpha((Float) animation.getAnimatedValue());

            }
        });
        ValueAnimator scale = ValueAnimator.ofFloat(1f, 1.5f);
        scale.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mIvBg.setScaleX((Float) animation.getAnimatedValue());
                mIvBg.setScaleY((Float) animation.getAnimatedValue());
                mIvLogo.setScaleX((Float) animation.getAnimatedValue());
                mIvLogo.setScaleY((Float) animation.getAnimatedValue());
            }
        });
        set.playTogether(alpha, scale);
        set.setDuration(500);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (UserInfoManager.INSTANCE.isLogin()) {
                    ActivityLauncher.startToMainActivity(getContext());
                } else {
                    ActivityLauncher.startToLoginActivity(getContext());
                }
                ActivityLauncher.transitionFadeIn(getContext());
                finish();
            }
        });
        set.start();
    }
}
