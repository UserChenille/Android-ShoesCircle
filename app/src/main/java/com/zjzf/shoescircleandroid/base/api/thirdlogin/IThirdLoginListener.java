package com.zjzf.shoescircleandroid.base.api.thirdlogin;

import com.zjzf.shoescircleandroid.base.api.thirdlogin.model.ThirdLoginInfo;

/**
 * Created by 陈志远 on 2018/7/23.
 */
public interface IThirdLoginListener<T extends ThirdLoginInfo> {

    void onStart();

    void onLoginSuccess(T data);

    void onLoginError(ThirdLoginException e);

    void onCancel();

}
