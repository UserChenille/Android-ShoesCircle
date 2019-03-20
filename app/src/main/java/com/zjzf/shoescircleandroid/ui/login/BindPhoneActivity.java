package com.zjzf.shoescircleandroid.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.net.client.NetClient;
import com.zjzf.shoescircle.lib.utils.PhoneUtil;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.lib.utils.TextWatcherAdapter;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.ui.widget.edit.ExEditText;
import com.zjzf.shoescircle.ui.widget.textview.button.ExButton;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.model.QQLoginInfo;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.model.ThirdLoginInfo;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.model.WechatLoginInfo;
import com.zjzf.shoescircleandroid.base.manager.UserInfoManager;
import com.zjzf.shoescircleandroid.base.net.apis.LoginApis;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 陈志远 on 2018/7/22.
 */
public class BindPhoneActivity extends BaseActivity {
    @BindView(R.id.ll_header)
    LinearLayout mLlHeader;
    @BindView(R.id.ed_phone)
    ExEditText mEdPhone;
    @BindView(R.id.ll_username)
    LinearLayout mLlUsername;
    @BindView(R.id.tv_warn)
    TextView mTvWarn;
    @BindView(R.id.ed_code)
    ExEditText mEdCode;
    @BindView(R.id.btn_send_code)
    ExButton mBtnSendCode;
    @BindView(R.id.ll_verifycode)
    LinearLayout mLlVerifycode;
    @BindView(R.id.btn_confirm)
    ExButton mBtnConfirm;

    CountDownTimer timer;

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onInitView(View decorView) {
        TextWatcherAdapter adapter = new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                checkAndSetConfirmEnable();
            }
        };
        mEdPhone.addTextChangedListener(adapter);
        mEdCode.addTextChangedListener(adapter);

    }

    private void checkAndSetConfirmEnable() {
        boolean enable = true;
        String phone = mEdPhone.getNonFormatText();
        if (TextUtils.isEmpty(phone)) {
            enable = false;
        }
        if (StringUtil.noEmpty(phone) && phone.length() >= 11 && !PhoneUtil.isPhoneNumber(phone)) {
            enable = false;
            ViewUtil.setViewsVisible(View.VISIBLE, mTvWarn);
        } else {
            ViewUtil.setViewsVisible(View.GONE, mTvWarn);
        }

        String code = mEdCode.getNonFormatText();
        if (TextUtils.isEmpty(code)) {
            enable = false;
        }

        mBtnConfirm.setEnabled(enable);

    }

    @OnClick(R.id.btn_send_code)
    void sendCode() {
        RetrofitClient.get()
                .create(LoginApis.class)
                .sendSmsCode(mEdPhone.getNonFormatText())
                .compose(this.<ObjectData>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData>() {
                    @Override
                    public void onNext(ObjectData result) {
                        UIHelper.toast("发送成功");
                        startCountDown();
                    }
                });

    }

    private void startCountDown() {
        if (timer != null) timer.cancel();
        timer = new CountDownTimer(45 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mBtnSendCode.setEnabled(false);
                mBtnSendCode.setText(String.format("%ds", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mBtnSendCode.setEnabled(true);
                mBtnSendCode.setText("重新发送");
            }
        };
        timer.start();
    }

    @OnClick(R.id.btn_confirm)
    void confirm() {
        ThirdLoginInfo thirdLoginInfo = UserInfoManager.INSTANCE.getThirdLoginInfo();
        if (thirdLoginInfo == null) {
            UIHelper.toast("获取第三方登录信息失败，请重试");
            return;
        }
        String unionid = "";
        String openId = "";
        int provider = 0;
        if (thirdLoginInfo instanceof WechatLoginInfo) {
            openId = ((WechatLoginInfo) thirdLoginInfo).getOpenid();
            unionid = ((WechatLoginInfo) thirdLoginInfo).getUnionid();
            provider = 1;
        } else if (thirdLoginInfo instanceof QQLoginInfo) {
            openId = ((QQLoginInfo) thirdLoginInfo).getOpenid();
            provider = 2;
        }
        /**
         * @param nick     昵称
         * @param avatar   头像
         * @param gender   性别（0:未知,1:男2：女）
         * @param unionid  微信union(provider为1时必填)
         * @param openId   微信openId或者QQopenId
         * @param provider 用户类型,1:微信用户，2：QQ用户
         * @param phone    手机号
         * @param smscode  验证码
         */

        RetrofitClient.get()
                .create(LoginApis.class)
                .register(NetClient.requestBody()
                        .put("nickName", thirdLoginInfo.getNickName())
                        .put("avatarUrl", thirdLoginInfo.getAvatarUrl())
                        .put("gender", thirdLoginInfo.getGender())
                        .put("unionid", unionid)
                        .put("openId", openId)
                        .put("provider", provider)
                        .put("phone", mEdPhone.getNonFormatText())
                        .put("smscode", mEdCode.getNonFormatText())
                        .build())
                .compose(this.<ObjectData<String>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData<String>>(this, true, true) {
                    @Override
                    public void onNext(ObjectData<String> result) {
                        UserInfoManager.INSTANCE.getUserInfo().setToken(result.getData());
                        UserInfoManager.INSTANCE.updateUserInfo();
                        ActivityLauncher.startToMainActivity(getContext());
                        finish();
                    }
                });

    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }
}
