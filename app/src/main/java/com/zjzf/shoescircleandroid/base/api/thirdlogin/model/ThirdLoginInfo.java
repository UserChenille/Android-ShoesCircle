package com.zjzf.shoescircleandroid.base.api.thirdlogin.model;

import com.zjzf.shoescircleandroid.model.ThirdLoginType;

import java.io.Serializable;

/**
 * Created by 陈志远 on 2018/7/23.
 */
public abstract class ThirdLoginInfo implements Serializable{
    private String nickName;
    private String avatarUrl;
    private int gender;//1:男，2：女
    protected ThirdLoginType mType;

    private String accessToken;
    private String imToken;

    public ThirdLoginInfo(ThirdLoginType type) {
        mType = type;
    }

    public String getNickName() {
        return nickName;
    }

    public ThirdLoginInfo setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public ThirdLoginInfo setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public int getGender() {
        return gender;
    }

    public ThirdLoginInfo setGender(int gender) {
        this.gender = gender;
        return this;
    }

    public ThirdLoginType getType() {
        return mType;
    }

    public ThirdLoginInfo setType(ThirdLoginType type) {
        mType = type;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getImToken() {
        return imToken;
    }

    public ThirdLoginInfo setImToken(String imToken) {
        this.imToken = imToken;
        return this;
    }

    public abstract boolean isLogin();
}
