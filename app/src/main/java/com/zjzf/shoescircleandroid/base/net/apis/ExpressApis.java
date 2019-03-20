package com.zjzf.shoescircleandroid.base.net.apis;

import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.model.express.ExpressCompanyInfo;
import com.zjzf.shoescircleandroid.model.express.ExpressInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 陈志远 on 2019/3/11.
 */
public interface ExpressApis {

    /**
     * 跟踪物流
     *
     * @param carrierCode 快递商编码
     * @param expressId   快递单号
     */
    @GET("/trackingmore/query")
    Observable<ObjectData<List<ExpressInfo>>> queryExpress(@Query("carrier_code") String carrierCode,
                                                           @Query("tracking_number") String expressId);

    /**
     * 通过订单号查找快递商
     *
     * @param expressId 快递单号
     */
    @GET("/trackingmore/queryCarrier")
    Observable<ObjectData<List<ExpressCompanyInfo>>> queryExpressCompany(@Query("tracking_number") String expressId);

    /**
     * 通过订单号查找快递商
     */
    @GET("/trackingmore/carriers")
    Observable<ObjectData<List<ExpressCompanyInfo>>> queryExpressCompanyList();

}
