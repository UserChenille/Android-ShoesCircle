package com.zjzf.shoescircleandroid.model.event;

import com.zjzf.shoescircleandroid.base.manager.RongYunManager;
import com.zjzf.shoescircleandroid.model.UserInfo;

/**
 * Created by 陈志远 on 2018/10/13.
 */
public class LoginSuccessEvent {
    private UserInfo mUserInfo;

    public LoginSuccessEvent(UserInfo userInfo) {
        this.mUserInfo = userInfo;
        RongYunManager.INSTANCE.connect();
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }
}
