package com.zjzf.shoescircleandroid.base.api.thirdlogin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tencent.connect.UserInfo;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zjzf.shoescircle.lib.base.AppContext;
import com.zjzf.shoescircle.lib.helper.RxHelper;
import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircleandroid.base.Define;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.model.QQLoginInfo;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.model.ThirdLoginInfo;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.model.WechatLoginInfo;
import com.zjzf.shoescircleandroid.base.manager.UserInfoManager;
import com.zjzf.shoescircleandroid.base.net.apis.LoginApis;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.model.ThirdLoginType;

import org.json.JSONObject;

import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by 陈志远 on 2018/7/23.
 */
public enum ThirdLoginManager {
    INSTANCE;


    private Tencent mQQManager;
    private IWXAPI mWechatManager;
    private Random mRandom = new Random();

    public void init(Application app) {
        mQQManager = Tencent.createInstance("1107389193", app.getApplicationContext());
        mWechatManager = WXAPIFactory.createWXAPI(app, Define.WECHAT_ID, true);
        mWechatManager.registerApp(Define.WECHAT_ID);
    }

    public Tencent getQQManager() {
        return mQQManager;
    }

    public IWXAPI getWechatManager() {
        return mWechatManager;
    }

    public <T extends ThirdLoginInfo> void login(Activity activity, ThirdLoginType type, IThirdLoginListener<T> listener) {
        switch (type) {
            case QQ:
                loginQQ(activity, listener);
                break;
            case WECHAT:
                loginWechat(listener);
                break;
        }
    }

    public <T extends ThirdLoginInfo> void login(Fragment fragment, ThirdLoginType type, IThirdLoginListener<T> listener) {
        switch (type) {
            case QQ:
                loginQQ(fragment, listener);
                break;
            case WECHAT:
                loginWechat(listener);
                break;
        }
    }

    private <T extends ThirdLoginInfo> void loginQQ(final Object context, final IThirdLoginListener<T> listener) {
        if (!(context instanceof Activity) && !(context instanceof Fragment)) {
            callError(listener, new ThirdLoginException("调用者必须是Activity或者Fragment"));
            return;
        }

        IUiListener uiListener = new IUiListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onComplete(Object o) {
                LogHelper.trace("QQ登录成功", String.valueOf(o));
                final JSONObject json = (JSONObject) o;
                Observable.create(new ObservableOnSubscribe<QQLoginInfo>() {
                    @Override
                    public void subscribe(final ObservableEmitter<QQLoginInfo> emitter) throws Exception {
                        final QQLoginInfo info = new QQLoginInfo();

                        final String openid = json.optString("openid");
                        final String assesToken = json.optString("access_token");
                        final String expiresIn = json.optString("expires_in");

                        mQQManager.setOpenId(openid);
                        mQQManager.setAccessToken(assesToken, expiresIn);
                        UserInfo userInfo = new UserInfo((Context) context, mQQManager.getQQToken());
                        userInfo.getUserInfo(new IUiListener() {
                            @Override
                            public void onComplete(Object o) {
                                info.setOpenid(openid)
                                        .setAssesToken(assesToken)
                                        .setExpiresIn(expiresIn);

                                /**
                                 * {
                                 "is_yellow_year_vip": "0",
                                 "ret": 0,
                                 "figureurl_qq_1":
                                 "http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/40",
                                 "figureurl_qq_2":
                                 "http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100",
                                 "nickname": "小罗",
                                 "yellow_vip_level": "0",
                                 "msg": "",
                                 "figureurl_1":
                                 "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/50",
                                 "vip": "0",
                                 "level": "0",
                                 "figureurl_2":
                                 "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100",
                                 "is_yellow_vip": "0",
                                 "gender": "男",
                                 "figureurl":
                                 "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/30"
                                 }
                                 */
                                JSONObject userInfoJson = (JSONObject) o;
                                LogHelper.trace("QQ用户信息",String.valueOf( userInfoJson));
                                info.setNickName(userInfoJson.optString("nickname"));
                                info.setAvatarUrl(userInfoJson.optString("figureurl_qq_2"));
                                info.setGender(TextUtils.equals(userInfoJson.optString("gender"), "男") ?
                                        1 : 2);
                                emitter.onNext(info);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(UiError uiError) {
                                callError(listener, new ThirdLoginException(uiError.errorMessage));
                            }

                            @Override
                            public void onCancel() {
                                UIHelper.toast("取消登录");

                            }
                        });
                    }
                }).compose(RxHelper.<QQLoginInfo>io_main())
                        .subscribe(new Consumer<QQLoginInfo>() {
                            @Override
                            public void accept(QQLoginInfo qqLoginInfo) throws Exception {
                                callSuccess(listener, qqLoginInfo);
                            }
                        });

                mQQManager.setAccessToken(json.optString("access_token"), json.optString("expires_in"));
            }

            @Override
            public void onError(UiError uiError) {
                callError(listener, new ThirdLoginException(uiError.errorDetail));
            }

            @Override
            public void onCancel() {
                callCancel(listener);
            }
        };

        if (context instanceof Activity) {
            mQQManager.login((Activity) context, "all", uiListener);
        } else {
            mQQManager.login((Fragment) context, "all", uiListener);
        }
        callStart(listener);
    }

    private volatile IThirdLoginListener mWechatListener;

    public boolean handleWechatResp(final BaseResp resp) {
        if (mWechatListener == null || resp == null) return false;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                loginWithWeChat(((SendAuth.Resp) resp).code);
                return true;
            default:
                mWechatListener = null;
                return false;
        }
    }

    private void loginWithWeChat(String code) {
        RetrofitClient.get()
                .create(LoginApis.class)
                .loginWithWechat(code)
                .compose(RetrofitClient.<ObjectData<WechatLoginInfo>>defaultTransformer())
                .subscribe(new OnResponseListener<ObjectData<WechatLoginInfo>>() {
                    @Override
                    public void onNext(ObjectData<WechatLoginInfo> result) {
                        if (result.getData() == null) {
                            callError(mWechatListener, new ThirdLoginException("返回数据为空"));
                        } else {
                            if (StringUtil.noEmpty(result.getData().getAccessToken())) {
                                UserInfoManager.INSTANCE.getUserInfo().setToken(result.getData().getAccessToken());
                            }
                            if (StringUtil.noEmpty(result.getData().getImToken())) {
                                UserInfoManager.INSTANCE.getUserInfo().setImToken(result.getData().getImToken());
                            }
                            UserInfoManager.INSTANCE.updateUserInfo();
                            callSuccess(mWechatListener, result.getData());
                        }
                        mWechatListener = null;
                    }

                });
    }

//    private void queryWechatInfo(String code) {
//        RetrofitClient.createNewClient("https://api.weixin.qq.com")
//                .create(LoginApis.class)
//                .getWechatToken(Define.WECHAT_ID, Define.WECHAT_SECRET, "authorization_code", code)
//                .compose(RxHelper.<WechatLoginInfo>io_main())
//                .subscribe(new OnResponseListener<WechatLoginInfo>() {
//                    @Override
//                    public void onNext(WechatLoginInfo result) {
//                        getWechatUserInfo(result);
//                    }
//
//                    @Override
//                    public void onFailure(ApiException e, int code, String message) {
//                        super.onFailure(e, code, message);
//                        mWechatListener = null;
//                    }
//                });
//    }

//    private void getWechatUserInfo(final WechatLoginInfo loginInfo) {
//        if (loginInfo == null) {
//            callError(mWechatListener, new ThirdLoginException("获取微信Token失败"));
//            mWechatListener = null;
//            return;
//        }
//        RetrofitClient.createNewClient("https://api.weixin.qq.com")
//                .create(LoginApis.class)
//                .getWechatUserInfo(loginInfo.getOpenid(), loginInfo.getAssesToken())
//                .compose(RxHelper.<WechatLoginInfo.WechatOriginLoginInfo>io_main())
//                .subscribe(new OnResponseListener<WechatLoginInfo.WechatOriginLoginInfo>() {
//                    @Override
//                    public void onNext(WechatLoginInfo.WechatOriginLoginInfo result) {
//
//                        loginInfo.setAvatarUrl(result.getHeadimgurl())
//                                .setGender(result.getSex() == 0 ? 1 : 2)
//                                .setNickName(result.getNickname());
//                        callSuccess(mWechatListener, loginInfo);
//                    }
//                });
//    }

    private <T extends ThirdLoginInfo> void loginWechat(IThirdLoginListener<T> listener) {
        if (!mWechatManager.isWXAppInstalled()) {
            UIHelper.toast("您还未安装微信客户端");
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = SystemClock.uptimeMillis() + mRandom.nextLong() + AppContext.getAppContext().getPackageName();
        mWechatManager.sendReq(req);
        mWechatListener = listener;
        callStart(listener);
    }

    private <T extends ThirdLoginInfo> void callStart(IThirdLoginListener<T> listener) {
        if (listener != null) {
            listener.onStart();
        }
    }

    private <T extends ThirdLoginInfo> void callCancel(IThirdLoginListener<T> listener) {
        if (listener != null) {
            listener.onCancel();
        }
    }

    private void callSuccess(IThirdLoginListener listener, ThirdLoginInfo info) {
        if (listener != null) {
            listener.onLoginSuccess(info);
            UserInfoManager.INSTANCE.setThirdLoginInfo(info);
            mWechatListener = null;
        }
    }

    private void callError(IThirdLoginListener listener, ThirdLoginException e) {
        if (listener != null) {
            listener.onLoginError(e);
        }
    }


}
