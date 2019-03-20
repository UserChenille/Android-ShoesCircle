package com.zjzf.shoescircleandroid.model.mygoods;

import com.google.gson.annotations.SerializedName;
import com.zjzf.shoescircle.lib.base.baseadapter.MultiType;

/**
 * Created by 陈志远 on 2019/3/6.
 */
public class GoodsWaitPayInfo implements MultiType {

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
     * expressId：快递id
     * addressInfo：收货地址信息
     * autoConfirmTime：自动确认收货时间
     * finishedTime：订单完成时间
     * tradeNum：交易次数
     * orderState:10(默认):未付款;20:已付款;30:已发货;40:已收货(已完成);
     * isCommented:已单是否已经评论 （0：没有 1：已评论）
     */
    /**
     * id : 1031099624340791300
     * enable : 1
     * createTime : 2018-08-19 08:44:42
     * paySn : null
     * memId : 1026362234707525600
     * buyer : null
     * sellerId : 1
     * seller : {"id":1,"enable":1,"createTime":"2018-07-08 08:46:54","account":"hehaoyang","phone":"15857193035","memName":"hehaoyang666","avatar":"","sex":0,"evaluate":5,"score":0,"level":1}
     * payType : 0
     * payTime : null
     * orderState : 10
     * orderAmount : 0.2
     * rewardAmount : 0.19
     * fee : 0.07
     * freightNo : c77124
     * name : null
     * photo : http://e-megasport.de/eng_pl_adidas-Superstar-Foundation-C77124-3440_1.jpg
     * size : 39
     * num : 1
     * expressId : null
     * shippingCode : null
     * addressId : null
     * autoConfirmTime : null
     * finishedTime : null
     * tradeNum : 6
     * isCommented : 1
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
    private int mSellerId;
    @SerializedName("seller")
    private SellerInfo mSeller;
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
    @SerializedName("expressId")
    private String mExpressId;
    @SerializedName("shippingCode")
    private String mShippingCode;
    @SerializedName("addressId")
    private String mAddressId;
    @SerializedName("autoConfirmTime")
    private String mAutoConfirmTime;
    @SerializedName("finishedTime")
    private String mFinishedTime;
    @SerializedName("tradeNum")
    private int mTradeNum;
    @SerializedName("isCommented")
    private int mIsCommented;
    @SerializedName("addressInfo")
    private String mAddressInfo;

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

    public int getSellerId() {
        return mSellerId;
    }

    public void setSellerId(int sellerId) {
        mSellerId = sellerId;
    }

    public SellerInfo getSeller() {
        return mSeller;
    }

    public void setSeller(SellerInfo seller) {
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

    public void setOrderAmount(double orderAmount) {
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

    public int getTradeNum() {
        return mTradeNum;
    }

    public void setTradeNum(int tradeNum) {
        mTradeNum = tradeNum;
    }


    public boolean isCommented(){
        return mIsCommented==1;
    }

    public void setIsCommented(int isCommented) {
        mIsCommented = isCommented;
    }

    public String getAddressInfo() {
        return mAddressInfo;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    public static class SellerInfo {
        /**
         * id : 1
         * enable : 1
         * createTime : 2018-07-08 08:46:54
         * account : hehaoyang
         * phone : 15857193035
         * memName : hehaoyang666
         * avatar :
         * sex : 0
         * evaluate : 5
         * score : 0
         * level : 1
         */

        @SerializedName("id")
        private int mId;
        @SerializedName("enable")
        private int mEnable;
        @SerializedName("createTime")
        private String mCreateTime;
        @SerializedName("account")
        private String mAccount;
        @SerializedName("phone")
        private String mPhone;
        @SerializedName("memName")
        private String mMemName;
        @SerializedName("avatar")
        private String mAvatar;
        @SerializedName("sex")
        private int mSex;
        @SerializedName("evaluate")
        private int mEvaluate;
        @SerializedName("score")
        private int mScore;
        @SerializedName("level")
        private int mLevel;

        public int getId() {
            return mId;
        }

        public void setId(int id) {
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

        public String getAccount() {
            return mAccount;
        }

        public void setAccount(String account) {
            mAccount = account;
        }

        public String getPhone() {
            return mPhone;
        }

        public void setPhone(String phone) {
            mPhone = phone;
        }

        public String getMemName() {
            return mMemName;
        }

        public void setMemName(String memName) {
            mMemName = memName;
        }

        public String getAvatar() {
            return mAvatar;
        }

        public void setAvatar(String avatar) {
            mAvatar = avatar;
        }

        public int getSex() {
            return mSex;
        }

        public void setSex(int sex) {
            mSex = sex;
        }

        public int getEvaluate() {
            return mEvaluate;
        }

        public void setEvaluate(int evaluate) {
            mEvaluate = evaluate;
        }

        public int getScore() {
            return mScore;
        }

        public void setScore(int score) {
            mScore = score;
        }

        public int getLevel() {
            return mLevel;
        }

        public void setLevel(int level) {
            mLevel = level;
        }
    }
}
