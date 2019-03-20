package com.zjzf.shoescircleandroid.base.net.apis;

import com.zjzf.shoescircleandroid.base.net.model.ObjectData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 陈志远 on 2019/3/10.
 */
public interface PayApis {
    /**
     * 请求支付
     */
    @POST("/xqOrder/pay")
    @FormUrlEncoded
    Observable<ObjectData<String>> requestAliPay(@Field("orderId") String orderId,
                                                 @Field("addressInfo") String addressInfo);
}
