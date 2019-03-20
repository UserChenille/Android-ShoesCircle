package com.zjzf.shoescircleandroid.base.api.thirdlogin.model;

import android.text.TextUtils;

import com.zjzf.shoescircleandroid.model.ThirdLoginType;

/**
 * Created by 陈志远 on 2018/7/23.
 */
public class WechatLoginInfo extends ThirdLoginInfo {
    private final int provider = 1;
    private String openid;
    private String unionid;

    public WechatLoginInfo() {
        super(ThirdLoginType.WECHAT);
    }

    public String getOpenid() {
        return openid;
    }

    public WechatLoginInfo setOpenid(String openid) {
        this.openid = openid;
        return this;
    }

    public String getUnionid() {
        return unionid;
    }

    public WechatLoginInfo setUnionid(String unionid) {
        this.unionid = unionid;
        return this;
    }

    public int getProvider() {
        return provider;
    }

    @Override
    public boolean isLogin() {
        return TextUtils.isEmpty(getAccessToken());
    }
}
