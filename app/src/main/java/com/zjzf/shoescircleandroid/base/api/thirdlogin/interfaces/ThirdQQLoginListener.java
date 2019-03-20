package com.zjzf.shoescircleandroid.base.api.thirdlogin.interfaces;

import com.zjzf.shoescircleandroid.base.api.thirdlogin.IThirdLoginListener;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.ThirdLoginException;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.model.QQLoginInfo;

/**
 * Created by 陈志远 on 2018/7/23.
 */
public abstract class ThirdQQLoginListener implements IThirdLoginListener<QQLoginInfo> {
    @Override
    public void onStart() {

    }

    @Override
    public void onLoginSuccess(QQLoginInfo data) {

    }

    @Override
    public void onLoginError(ThirdLoginException e) {

    }

    @Override
    public void onCancel() {

    }
}
