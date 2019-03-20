package com.zjzf.shoescircleandroid.base.net.apis;

import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.model.UserDetailInfo;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by 陈志远 on 2018/10/15.
 */
public interface UserApis {

    /**
     * 用户信息详情
     */
    @GET("/xqMember/my")
    Observable<ObjectData<UserDetailInfo>> getUserDetailInfo();


    /**
     * 绑定支付宝
     */
    @POST("/xqMember/bindAliPayAccount")
    @FormUrlEncoded
    Observable<ObjectData> bindAliPay(@Field("realName") String realName,
                                      @Field("aliPayAccount") String aliPayAccount);

    @GET("/xqMember/{userId}")
    Observable<ObjectData<UserDetailInfo>> getUserById(@Path("userId") String userid);

}
