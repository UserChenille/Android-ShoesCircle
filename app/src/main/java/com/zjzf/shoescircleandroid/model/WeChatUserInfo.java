package com.zjzf.shoescircleandroid.model;

import java.io.Serializable;

/**
 * Created by 陈志远 on 2018/10/15.
 */
public class WeChatUserInfo implements Serializable {
    private String token;
    private String nickName;
    private String avatarUrl;
    private int gender;
    private String unionid;
    private String openid;
    private final int provider = 1;

    public String getToken() {
        return token;
    }

    public WeChatUserInfo setToken(String token) {
        this.token = token;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public WeChatUserInfo setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public WeChatUserInfo setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public int getGender() {
        return gender;
    }

    public WeChatUserInfo setGender(int gender) {
        this.gender = gender;
        return this;
    }

    public String getUnionid() {
        return unionid;
    }

    public WeChatUserInfo setUnionid(String unionid) {
        this.unionid = unionid;
        return this;
    }

    public String getOpenid() {
        return openid;
    }

    public WeChatUserInfo setOpenid(String openid) {
        this.openid = openid;
        return this;
    }

    public int getProvider() {
        return provider;
    }
}
