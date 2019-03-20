package com.zjzf.shoescircleandroid.base.net.apis;

import com.zjzf.shoescircleandroid.base.api.thirdlogin.model.WechatLoginInfo;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by 陈志远 on 2018/8/5.
 */
public interface LoginApis {

    /**
     * QQ登录
     * 用户类型,1:微信用户，2：QQ用户)
     */
    @GET("/callback/qq")
    Observable<ObjectData<String>> loginWithQQ(@Query("code") String code);

    /**
     * wechat登录
     * 用户类型,1:微信用户，2：QQ用户)
     */
    @GET("/callback/wx")
    Observable<ObjectData<WechatLoginInfo>> loginWithWechat(@Query("code") String code);

    /**
     * 获取微信token，需要使用新的retrofit
     */
    @GET("/sns/oauth2/access_token")
    Observable<WechatLoginInfo> getWechatToken(@Query("appid") String appid,
                                               @Query("secret") String secret,
                                               @Query("grant_type") String grant_type,
                                               @Query("code") String code);

//    /**
//     * 获取微信个人信息，需要使用新的retrofit
//     */
//    @GET("/sns/userinfo")
//    Observable<WechatLoginInfo.WechatOriginLoginInfo> getWechatUserInfo(@Query("openid") String openid,
//                                                                        @Query("access_token") String token);

    /**
     * 注册短信
     */
    @POST("/smscode")
    @FormUrlEncoded
    Observable<ObjectData> sendSmsCode(@Field("phone") String phone);

    /**
     * 注册
     */
    @POST("/regin")
    Observable<ObjectData<String>> register(@Body RequestBody requestBody);

}
