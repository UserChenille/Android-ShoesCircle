package com.zjzf.shoescircleandroid.model;

import com.tencent.bugly.crashreport.CrashReport;
import com.zjzf.shoescircle.lib.helper.RxHelper;
import com.zjzf.shoescircle.lib.interfaces.SimpleCallback;
import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircleandroid.base.manager.RongYunManager;
import com.zjzf.shoescircleandroid.base.manager.UserInfoManager;
import com.zjzf.shoescircleandroid.base.net.apis.UserApis;
import com.zjzf.shoescircleandroid.base.net.client.HandleServerApiErrorAction;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;

import java.io.Serializable;

/**
 * Created by 陈志远 on 2018/9/13.
 */
public class UserInfo implements Serializable {
    private String token;
    private String imToken;
    private String nickName;
    private String avatarUrl;
    private int gender;

    private volatile UserDetailInfo mUserDetailInfo;


    public boolean isLogin() {
        return StringUtil.noEmpty(token);
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDetailInfo getUserDetailInfo() {
        if (mUserDetailInfo == null) {
            getUserDetailInfo(null);
        }
        return mUserDetailInfo;
    }

    public String getImToken() {
        return imToken;
    }

    public UserInfo setImToken(String imToken) {
        this.imToken = imToken;
        return this;
    }

    public void getUserDetailInfo(final SimpleCallback<UserDetailInfo> callback) {
        getUserDetailInfo(callback, false);
    }

    public void getUserFee(final SimpleCallback<Double> fee) {
        getUserDetailInfo(new SimpleCallback<UserDetailInfo>() {
            @Override
            public void onCall(UserDetailInfo data) {
                if (fee != null) {
                    if (data.getXqMemlevel() != null) {
                        fee.onCall(data.getXqMemlevel().getFee());
                    }
                }
            }
        });
    }

    public void getUserDetailInfo(final SimpleCallback<UserDetailInfo> callback, boolean refreshFromNet) {
        if (mUserDetailInfo != null && !refreshFromNet) {
            if (callback != null) {
                callback.onCall(mUserDetailInfo);
            }
        } else {
            RetrofitClient.get()
                    .create(UserApis.class)
                    .getUserDetailInfo()
                    .doOnNext(new HandleServerApiErrorAction<>())
                    .compose(RxHelper.<ObjectData<UserDetailInfo>>io_main())
                    .subscribe(new OnResponseListener<ObjectData<UserDetailInfo>>(false) {
                        @Override
                        public void onNext(ObjectData<UserDetailInfo> result) {
                            mUserDetailInfo = result.getData();
                            if (mUserDetailInfo != null) {
                                nickName = mUserDetailInfo.getMemName();
                                avatarUrl = mUserDetailInfo.getAvatar();
                                gender = mUserDetailInfo.getSex();
                                RongYunManager.INSTANCE.setCurUser(mUserDetailInfo);
                            }
                            if (callback != null) {
                                callback.onCall(mUserDetailInfo);
                            }
                        }
                    });
        }
    }


    public UserInfo setUserDetailInfo(UserDetailInfo userDetailInfo) {
        mUserDetailInfo = userDetailInfo;
        return this;
    }

    public UserInfo updateData() {
        getUserDetailInfo(new SimpleCallback<UserDetailInfo>() {
            @Override
            public void onCall(UserDetailInfo data) {
                mUserDetailInfo = data;
                UserInfoManager.INSTANCE.saveUserInfo();
            }
        }, true);
        if (mUserDetailInfo != null) {
            RongYunManager.INSTANCE.setCurUser(mUserDetailInfo);
            LogHelper.trace("用户信息", mUserDetailInfo.toString());
            CrashReport.setUserId(mUserDetailInfo.getPhone());
        }
        return this;
    }
}
