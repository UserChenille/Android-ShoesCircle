package com.zjzf.shoescircleandroid.base.api.thirdlogin.interfaces;

import com.zjzf.shoescircleandroid.base.api.thirdlogin.IThirdLoginListener;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.ThirdLoginException;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.model.WechatLoginInfo;

/**
 * Created by 陈志远 on 2018/7/23.
 */
public abstract class ThirdWeLoginListener implements IThirdLoginListener<WechatLoginInfo> {
    @Override
    public void onStart() {

    }

    @Override
    public void onLoginSuccess(WechatLoginInfo data) {

    }

    @Override
    public void onLoginError(ThirdLoginException e) {

    }

    @Override
    public void onCancel() {

    }
}
