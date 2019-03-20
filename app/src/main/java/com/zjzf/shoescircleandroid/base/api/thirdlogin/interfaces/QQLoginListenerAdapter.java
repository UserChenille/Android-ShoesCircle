package com.zjzf.shoescircleandroid.base.api.thirdlogin.interfaces;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by 陈志远 on 2018/7/23.
 */
public abstract class QQLoginListenerAdapter implements IUiListener{
    @Override
    public void onComplete(Object o) {

    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }

    public static class EmptyQQloginListener extends QQLoginListenerAdapter{

    }
}
