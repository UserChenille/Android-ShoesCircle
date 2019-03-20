package com.zjzf.shoescircleandroid.base.net.apis;

import com.zjzf.shoescircleandroid.base.net.model.ListData;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.model.AutoInputInfo;
import com.zjzf.shoescircleandroid.model.CommentInfo;
import com.zjzf.shoescircleandroid.model.GoodsDetailInfo;
import com.zjzf.shoescircleandroid.model.GoodsImageInfo;
import com.zjzf.shoescircleandroid.model.GoodsListInfo;
import com.zjzf.shoescircleandroid.model.GoodsSellInfo;
import com.zjzf.shoescircleandroid.model.SearchResultInfo;
import com.zjzf.shoescircleandroid.model.mygoods.GoodsDemandProcessInfo;
import com.zjzf.shoescircleandroid.model.mygoods.GoodsRemovedInfo;
import com.zjzf.shoescircleandroid.model.mygoods.GoodsWaitPayInfo;
import com.zjzf.shoescircleandroid.model.order.OrderDetail;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by 陈志远 on 2018/8/5.
 */
public interface GoodsApis {

    /**
     * 求货列表
     */
    @GET("/xqGoods/list")
    Observable<ObjectData<GoodsListInfo>> getGoodsList(@Query("pageNum") int page, @Query("pageSize") int count);


    /**
     * 求货列表
     */
    @GET("/xqGoods/search/list")
    Observable<ListData<SearchResultInfo>> searchGoodsList(@Query("content") String key);

    /**
     * 求货详情
     */
    @GET("/xqGoods/{id}")
    Observable<ObjectData<GoodsDetailInfo>> getGoodsDetail(@Path("id") String id);


    /**
     * 模糊搜索货号等
     *
     * @param page       页码
     * @param pageSize   每页大小
     * @param freight_no 货号
     * @param name       名称
     */
    @GET("/xqGoods/freightNoOrName")
    Observable<ListData<AutoInputInfo>> queryAutoInput(@Query("pageNum") int page,
                                                       @Query("pageSize") int pageSize,
                                                       @Query("freight_no") String freight_no,
                                                       @Query("name") String name);


    /**
     * 发布求货
     */
    @POST("/xqGoods")
    Observable<ObjectData> postGoods(@Body RequestBody requestBody);

    /**
     * 搜索图片
     *
     * @param artNo 货号
     */
    @GET("/xqGoods/image")
    Observable<ObjectData<List<GoodsImageInfo>>> searchGoodsImages(@Query("name") String artNo);


    /**
     * 我的求货 - 求货中
     */
    @GET("/xqMember/goods/list")
    Observable<ListData<GoodsDemandProcessInfo>> getGoodsDemandProcessList(@Query("pageNum") int page,
                                                                           @Query("pageSize") int pageSize);


    /**
     * 我的求货-未付款/已付款
     *
     * @param state 10(默认):未付款;20:已付款
     */
    @GET("/xqOrder/buy/list")
    Observable<ListData<GoodsWaitPayInfo>> getGoodsWaitPayList(@Query("orderState") int state);

    /**
     * 我的求货-已下架
     */
    @GET("/xqGoods/offShelfList")
    Observable<ListData<GoodsRemovedInfo>> getGoodsRemovedList();

    /**
     * 我的出售
     *
     * @param orderState 10(默认):未付款;20:已付款;30:已发货;40:已收货(已完成);
     */
    @GET("/xqOrder/sell/list")
    Observable<ListData<GoodsSellInfo>> getGoodsSellListInfo(@Query("orderState") int orderState);

    /**
     * 下架
     *
     * @param orderId
     * @return
     */
    @PUT("/xqGoods/{id}/offShelf")
    Observable<ObjectData> offSell(@Path("id") String orderId);

    /**
     * 发起收款
     */
    @POST("/xqOrder")
    Observable<ObjectData<String>> createOrder(@Body RequestBody requestBody);

    /**
     * 订单详情
     */
    @GET("/xqOrder/{id}")
    Observable<ObjectData<OrderDetail>> getOrderDetail(@Path("id") String id);

    /**
     * 发货
     *
     * @param orderId      订单id
     * @param expressCode  快递编码
     * @param shippingCode 快递单号
     */
    @POST("/xqOrder/deliver")
    @FormUrlEncoded
    Observable<ObjectData<String>> deliverGoods(@Field("orderId") String orderId,
                                                @Field("expressCode") String expressCode,
                                                @Field("shippingCode") String shippingCode);

    @GET("/xqMemComment")
    Observable<ListData<CommentInfo>> getCommentList(@Query("sellerId") String sellerId);
}
