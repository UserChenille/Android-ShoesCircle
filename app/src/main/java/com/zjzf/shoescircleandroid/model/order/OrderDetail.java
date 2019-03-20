package com.zjzf.shoescircleandroid.model.order;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 陈志远 on 2019/3/6.
 */
public class OrderDetail {
/**
 * payType:支付类型(1:支付宝 2:微信 3:银联)
 * paySn:支付宝或微信支付凭证编号
 * orderAmount:订单价格
 * rewardAmount:扣除手续费的价格
 * fee:手续费
 * freightNo：商品货号
 * name：商品名称
 * photo：商品图片
 * num：商品数量
 * shippingCode：物流单号
 * expressId：快递公司编号
 * addressInfo：收货地址信息
 * autoConfirmTime：自动确认收货时间
 * finishedTime：订单完成时间
 * deadLineTime：截止发货日期
 * remainingDeliverySeconds：截止发货秒数
 */
    /**
     * id : 1058602762723348500
     * enable : 1
     * createTime : 2018-11-03 14:12:21
     * paySn : null
     * memId : 1026362234707525600
     * buyer : null
     * sellerId : 1
     * seller : null
     * payType : 0
     * payTime : null
     * orderState : 10 // 20已付款 30已发货  40已完成  10待付款
     * orderAmount : 1
     * rewardAmount : 0.93
     * fee : 0.07
     * freightNo : 111
     * name : null
     * photo : null
     * size : 22
     * num : 1
     * expressId : null
     * shippingCode : null
     * addressId : null
     * addressInfo : null
     * autoConfirmTime : null
     * finishedTime : null
     * deadLineTime : 2018-11-04 14:12:21
     * remainingDeliverySeconds : 3600
     */

    @SerializedName("id")
    private long mId;
    @SerializedName("enable")
    private int mEnable;
    @SerializedName("createTime")
    private String mCreateTime;
    @SerializedName("paySn")
    private String mPaySn;
    @SerializedName("memId")
    private long mMemId;
    @SerializedName("buyer")
    private String mBuyer;
    @SerializedName("sellerId")
    private String mSellerId;
    @SerializedName("seller")
    private String mSeller;
    @SerializedName("payType")
    private int mPayType;
    @SerializedName("payTime")
    private String mPayTime;
    @SerializedName("orderState")
    private int mOrderState;
    @SerializedName("orderAmount")
    private double mOrderAmount;
    @SerializedName("rewardAmount")
    private double mRewardAmount;
    @SerializedName("fee")
    private double mFee;
    @SerializedName("freightNo")
    private String mFreightNo;
    @SerializedName("name")
    private String mName;
    @SerializedName("photo")
    private String mPhoto;
    @SerializedName("size")
    private String mSize;
    @SerializedName("num")
    private int mNum;
    @SerializedName("expressCode")
    private String mExpressId;
    @SerializedName("shippingCode")
    private String mShippingCode;
    @SerializedName("addressId")
    private String mAddressId;
    @SerializedName("addressInfo")
    private String mAddressInfo;
    @SerializedName("autoConfirmTime")
    private String mAutoConfirmTime;
    @SerializedName("finishedTime")
    private String mFinishedTime;
    @SerializedName("deadLineTime")
    private String mDeadLineTime;
    @SerializedName("remainingDeliverySeconds")
    private int mRemainingDeliverySeconds;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public int getEnable() {
        return mEnable;
    }

    public void setEnable(int enable) {
        mEnable = enable;
    }

    public String getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(String createTime) {
        mCreateTime = createTime;
    }

    public String getPaySn() {
        return mPaySn;
    }

    public void setPaySn(String paySn) {
        mPaySn = paySn;
    }

    public long getMemId() {
        return mMemId;
    }

    public void setMemId(long memId) {
        mMemId = memId;
    }

    public String getBuyer() {
        return mBuyer;
    }

    public void setBuyer(String buyer) {
        mBuyer = buyer;
    }

    public String getSellerId() {
        return mSellerId;
    }

    public void setSellerId(String sellerId) {
        mSellerId = sellerId;
    }

    public String getSeller() {
        return mSeller;
    }

    public void setSeller(String seller) {
        mSeller = seller;
    }

    public int getPayType() {
        return mPayType;
    }

    public void setPayType(int payType) {
        mPayType = payType;
    }

    public String getPayTime() {
        return mPayTime;
    }

    public void setPayTime(String payTime) {
        mPayTime = payTime;
    }

    public int getOrderState() {
        return mOrderState;
    }

    public void setOrderState(int orderState) {
        mOrderState = orderState;
    }

    public double getOrderAmount() {
        return mOrderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        mOrderAmount = orderAmount;
    }

    public double getRewardAmount() {
        return mRewardAmount;
    }

    public void setRewardAmount(double rewardAmount) {
        mRewardAmount = rewardAmount;
    }

    public double getFee() {
        return mFee;
    }

    public void setFee(double fee) {
        mFee = fee;
    }

    public String getFreightNo() {
        return mFreightNo;
    }

    public void setFreightNo(String freightNo) {
        mFreightNo = freightNo;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        mSize = size;
    }

    public int getNum() {
        return mNum;
    }

    public void setNum(int num) {
        mNum = num;
    }

    public String getExpressId() {
        return mExpressId;
    }

    public void setExpressId(String expressId) {
        mExpressId = expressId;
    }

    public String getShippingCode() {
        return mShippingCode;
    }

    public void setShippingCode(String shippingCode) {
        mShippingCode = shippingCode;
    }

    public String getAddressId() {
        return mAddressId;
    }

    public void setAddressId(String addressId) {
        mAddressId = addressId;
    }

    public String getAddressInfo() {
        return mAddressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        mAddressInfo = addressInfo;
    }

    public String getAutoConfirmTime() {
        return mAutoConfirmTime;
    }

    public void setAutoConfirmTime(String autoConfirmTime) {
        mAutoConfirmTime = autoConfirmTime;
    }

    public String getFinishedTime() {
        return mFinishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        mFinishedTime = finishedTime;
    }

    public String getDeadLineTime() {
        return mDeadLineTime;
    }

    public void setDeadLineTime(String deadLineTime) {
        mDeadLineTime = deadLineTime;
    }

    public int getRemainingDeliverySeconds() {
        return mRemainingDeliverySeconds;
    }

    public void setRemainingDeliverySeconds(int remainingDeliverySeconds) {
        mRemainingDeliverySeconds = remainingDeliverySeconds;
    }
}
