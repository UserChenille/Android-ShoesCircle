package com.zjzf.shoescircleandroid.base.api.thirdlogin.model;

import android.text.TextUtils;

import com.zjzf.shoescircleandroid.model.ThirdLoginType;

/**
 * Created by 陈志远 on 2018/7/23.
 */
public class QQLoginInfo extends ThirdLoginInfo {
    private String openid;
    private String assesToken;
    private String expiresIn;

    public QQLoginInfo() {
        super(ThirdLoginType.QQ);
    }

    public String getOpenid() {
        return openid;
    }

    public QQLoginInfo setOpenid(String openid) {
        this.openid = openid;
        return this;
    }

    public String getAssesToken() {
        return assesToken;
    }

    public QQLoginInfo setAssesToken(String assesToken) {
        this.assesToken = assesToken;
        return this;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public QQLoginInfo setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    @Override
    public boolean isLogin() {
        return TextUtils.isEmpty(assesToken);
    }
}
