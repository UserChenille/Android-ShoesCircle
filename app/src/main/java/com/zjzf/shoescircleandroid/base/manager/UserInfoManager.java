package com.zjzf.shoescircleandroid.base.manager;

import com.zjzf.shoescircle.lib.net.client.NetClient;
import com.zjzf.shoescircle.lib.tools.gson.GsonUtil;
import com.zjzf.shoescircle.lib.utils.EventBusUtil;
import com.zjzf.shoescircleandroid.base.AppSettingManager;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.model.ThirdLoginInfo;
import com.zjzf.shoescircleandroid.model.UserInfo;
import com.zjzf.shoescircleandroid.model.event.LoginSuccessEvent;

/**
 * Created by 陈志远 on 2018/7/22.
 */
public enum UserInfoManager {
    INSTANCE;

    private volatile UserInfo mUserInfo;

    private volatile ThirdLoginInfo mThirdLoginInfo;

    public void init() {
        checkUserInfo();
    }

    private void checkUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = GsonUtil.INSTANCE.toObject(AppSettingManager.loadString(AppSettingManager.KEY_USER, ""), UserInfo.class);
            if (mUserInfo != null) {
                NetClient.sUserToken = mUserInfo.getToken();
                mUserInfo.updateData();
                onLogSuccess();
            }
        }
        if (mUserInfo == null) {
            mUserInfo = new UserInfo();
        }
    }

    public boolean isLogin() {
        checkUserInfo();
        return mUserInfo.isLogin();
    }

    public UserInfo getUserInfo() {
        checkUserInfo();
        return mUserInfo;
    }

    public UserInfoManager setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        updateUserInfo();
        return this;
    }

    public ThirdLoginInfo getThirdLoginInfo() {
        return mThirdLoginInfo;
    }

    public UserInfoManager setThirdLoginInfo(ThirdLoginInfo thirdLoginInfo) {
        mThirdLoginInfo = thirdLoginInfo;
        return this;
    }

    public void updateUserInfo() {
        checkUserInfo();
        AppSettingManager.saveString(AppSettingManager.KEY_USER, GsonUtil.INSTANCE.toString(mUserInfo));
        onLogSuccess();
    }

    public void saveUserInfo(){
        AppSettingManager.saveString(AppSettingManager.KEY_USER, GsonUtil.INSTANCE.toString(mUserInfo));
    }

    public void onLogSuccess() {
        if (mUserInfo != null) {
            NetClient.sUserToken = mUserInfo.getToken();
            mUserInfo.updateData();
            EventBusUtil.post(new LoginSuccessEvent(mUserInfo));
        }
    }
}
