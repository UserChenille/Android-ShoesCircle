package com.zjzf.shoescircleandroid.ui.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.utils.MultiSpanUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.widget.textview.button.ExButton;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.ThirdLoginException;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.ThirdLoginManager;
import com.zjzf.shoescircleandroid.model.ThirdLoginType;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.interfaces.ThirdQQLoginListener;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.interfaces.ThirdWeLoginListener;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.model.QQLoginInfo;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.model.WechatLoginInfo;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 陈志远 on 2018/7/22.
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.ll_header)
    LinearLayout mLlHeader;
    @BindView(R.id.iv_logo)
    ImageView mIvLogo;
    @BindView(R.id.tv_tips)
    TextView mTvTips;
    @BindView(R.id.btn_qq_login)
    ExButton mBtnQqLogin;
    @BindView(R.id.btn_wechat_login)
    ExButton mBtnWechatLogin;

    private static final long ANIMATE_DURATION = 450;

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onInitView(View decorView) {
        MultiSpanUtil.create(R.string.login_tips)
                .append("用户服务协议")
                .setTextType(Typeface.DEFAULT_BOLD)
                .setTextColorFromRes(R.color.common_black)
                .setSpanClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .into(mTvTips);
        mTvTips.setTranslationY(UIHelper.dip2px(50));
        mBtnQqLogin.setTranslationY(UIHelper.dip2px(50));
        mBtnWechatLogin.setTranslationY(UIHelper.dip2px(50));
        startAnim();
    }

    private void startAnim() {
        long startTimeDelay = 500;
        long delay = (long) (ANIMATE_DURATION * 0.3);
        mIvLogo.animate()
                .alpha(1f)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(ANIMATE_DURATION)
                .setStartDelay(startTimeDelay)
                .start();
        mTvTips.animate()
                .alpha(1f)
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(ANIMATE_DURATION)
                .setStartDelay(startTimeDelay + delay)
                .start();
        mBtnQqLogin.animate()
                .alpha(1f)
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setStartDelay(startTimeDelay + 2 * delay)
                .setDuration(ANIMATE_DURATION)
                .start();
        mBtnWechatLogin.animate()
                .alpha(1f)
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setStartDelay(startTimeDelay + 3 * delay)
                .setDuration(ANIMATE_DURATION)
                .start();
    }

    @OnClick(R.id.btn_qq_login)
    void loginByQQ() {
        ThirdLoginManager.INSTANCE.login(this, ThirdLoginType.QQ, new ThirdQQLoginListener() {
            @Override
            public void onLoginSuccess(QQLoginInfo data) {
                super.onLoginSuccess(data);
            }

            @Override
            public void onLoginError(ThirdLoginException e) {
                super.onLoginError(e);
                UIHelper.toast(e.getMessage());
            }
        });

    }


    @OnClick(R.id.btn_wechat_login)
    void loginByWechat() {
//        ActivityLauncher.startToMainActivity(getContext());
        ThirdLoginManager.INSTANCE.login(this, ThirdLoginType.WECHAT, new ThirdWeLoginListener() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onLoginSuccess(WechatLoginInfo data) {
                super.onLoginSuccess(data);
                dismissLoadingDialog();
                if (TextUtils.isEmpty(data.getAccessToken())) {
                    ActivityLauncher.startToBindPhoneActivity(getContext());
                } else {
                    ActivityLauncher.startToMainActivity(getContext());
                    finish();
                }
            }

            @Override
            public void onLoginError(ThirdLoginException e) {
                super.onLoginError(e);
                dismissLoadingDialog();
                UIHelper.toast(e.getMessage());
            }
        });

    }

}
