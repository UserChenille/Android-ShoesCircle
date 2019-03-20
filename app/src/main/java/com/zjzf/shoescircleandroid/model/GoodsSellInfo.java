package com.zjzf.shoescircleandroid.model;

import com.zjzf.shoescircle.lib.base.baseadapter.MultiType;

/**
 * Created by 陈志远 on 2018/11/25.
 */
public class GoodsSellInfo implements MultiType {


    /**
     * id : 1031099624340791300
     * enable : 1
     * createTime : 2018-08-19 08:44:42
     * paySn : null
     * memId : 1026362234707525600
     * buyer : {"id":1026362234707525600,"enable":1,"createTime":"2018-08-06 07:00:00","account":"zY158571930368547","phone":"15857193036","memName":"上帝","avatar":"http://www.baidu.com","sex":1,"evaluate":5,"score":0,"level":1}
     * sellerId : 1
     * seller : null
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
     */

    private String id;
    private int enable;
    private String createTime;
    private String paySn;
    private long memId;
    private BuyerBean buyer;
    private int sellerId;
    private String seller;
    private int payType;
    private String payTime;
    private int orderState;
    private double orderAmount;
    private double rewardAmount;
    private double fee;
    private String freightNo;
    private String name;
    private String photo;
    private String size;
    private int num;
    private String expressId;
    private String shippingCode;
    private String addressId;
    private String autoConfirmTime;
    private String finishedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPaySn() {
        return paySn;
    }

    public void setPaySn(String paySn) {
        this.paySn = paySn;
    }

    public long getMemId() {
        return memId;
    }

    public void setMemId(long memId) {
        this.memId = memId;
    }

    public BuyerBean getBuyer() {
        return buyer;
    }

    public void setBuyer(BuyerBean buyer) {
        this.buyer = buyer;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public double getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(double rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getFreightNo() {
        return freightNo;
    }

    public void setFreightNo(String freightNo) {
        this.freightNo = freightNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAutoConfirmTime() {
        return autoConfirmTime;
    }

    public void setAutoConfirmTime(String autoConfirmTime) {
        this.autoConfirmTime = autoConfirmTime;
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    public static class BuyerBean {
        /**
         * id : 1026362234707525600
         * enable : 1
         * createTime : 2018-08-06 07:00:00
         * account : zY158571930368547
         * phone : 15857193036
         * memName : 上帝
         * avatar : http://www.baidu.com
         * sex : 1
         * evaluate : 5
         * score : 0
         * level : 1
         */

        private long id;
        private int enable;
        private String createTime;
        private String account;
        private String phone;
        private String memName;
        private String avatar;
        private int sex;
        private int evaluate;
        private int score;
        private int level;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getEnable() {
            return enable;
        }

        public void setEnable(int enable) {
            this.enable = enable;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMemName() {
            return memName;
        }

        public void setMemName(String memName) {
            this.memName = memName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getEvaluate() {
            return evaluate;
        }

        public void setEvaluate(int evaluate) {
            this.evaluate = evaluate;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}
