package com.zjzf.shoescircleandroid.model;

import com.google.gson.annotations.SerializedName;
import com.zjzf.shoescircle.lib.base.baseadapter.MultiType;

/**
 * Created by 陈志远 on 2019/3/13.
 */
public class CommentInfo implements MultiType {

    /**
     * id : 1097764902799908900
     * enable : null
     * createTime : 2019-02-19 15:48:43
     * buyerId : 1052218251554304000
     * buyer : {"id":1052218251554304000,"enable":1,"createTime":"2018-10-16 23:22:35","account":"jB135704895171884","phone":"13570489517","memName":"å¤§ç\u0081¯æ³¡æµ\u008bè¯\u0095","avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/68lgd9UARxY8vEw0gBnJKehoa7StsibKsDs1o7BW6XFZblfjruGWTBfPicAjwHhV71uXmicRRoQoX11Yz8dvAVPCQ/132","sex":2,"evaluate":5,"score":0,"level":1,"upgradeScore":null,"xqMemlevel":null,"alipayAccount":null,"realName":null,"tradeNum":0}
     * seller : null
     * sellerId : 1056086672276697100
     * orderId : 1071935132859056100
     * score : 3
     * commentContent : fsfasdfasfasdf
     */

    @SerializedName("id")
    private long mId;
    @SerializedName("enable")
    private String mEnable;
    @SerializedName("createTime")
    private String mCreateTime;
    @SerializedName("buyerId")
    private long mBuyerId;
    @SerializedName("buyer")
    private BuyerInfo mBuyer;
    @SerializedName("seller")
    private String mSeller;
    @SerializedName("sellerId")
    private long mSellerId;
    @SerializedName("orderId")
    private long mOrderId;
    @SerializedName("score")
    private int mScore;
    @SerializedName("commentContent")
    private String mCommentContent;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getEnable() {
        return mEnable;
    }

    public void setEnable(String enable) {
        mEnable = enable;
    }

    public String getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(String createTime) {
        mCreateTime = createTime;
    }

    public long getBuyerId() {
        return mBuyerId;
    }

    public void setBuyerId(long buyerId) {
        mBuyerId = buyerId;
    }

    public BuyerInfo getBuyer() {
        return mBuyer;
    }

    public void setBuyer(BuyerInfo buyer) {
        mBuyer = buyer;
    }

    public String getSeller() {
        return mSeller;
    }

    public void setSeller(String seller) {
        mSeller = seller;
    }

    public long getSellerId() {
        return mSellerId;
    }

    public void setSellerId(long sellerId) {
        mSellerId = sellerId;
    }

    public long getOrderId() {
        return mOrderId;
    }

    public void setOrderId(long orderId) {
        mOrderId = orderId;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public String getCommentContent() {
        return mCommentContent;
    }

    public void setCommentContent(String commentContent) {
        mCommentContent = commentContent;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    public static class BuyerInfo {
        /**
         * id : 1052218251554304000
         * enable : 1
         * createTime : 2018-10-16 23:22:35
         * account : jB135704895171884
         * phone : 13570489517
         * memName : å¤§ç¯æ³¡æµè¯
         * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/68lgd9UARxY8vEw0gBnJKehoa7StsibKsDs1o7BW6XFZblfjruGWTBfPicAjwHhV71uXmicRRoQoX11Yz8dvAVPCQ/132
         * sex : 2
         * evaluate : 5
         * score : 0
         * level : 1
         * upgradeScore : null
         * xqMemlevel : null
         * alipayAccount : null
         * realName : null
         * tradeNum : 0
         */

        @SerializedName("id")
        private long mId;
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
        @SerializedName("upgradeScore")
        private String mUpgradeScore;
        @SerializedName("xqMemlevel")
        private String mXqMemlevel;
        @SerializedName("alipayAccount")
        private String mAlipayAccount;
        @SerializedName("realName")
        private String mRealName;
        @SerializedName("tradeNum")
        private int mTradeNum;

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

        public String getUpgradeScore() {
            return mUpgradeScore;
        }

        public void setUpgradeScore(String upgradeScore) {
            mUpgradeScore = upgradeScore;
        }

        public String getXqMemlevel() {
            return mXqMemlevel;
        }

        public void setXqMemlevel(String xqMemlevel) {
            mXqMemlevel = xqMemlevel;
        }

        public String getAlipayAccount() {
            return mAlipayAccount;
        }

        public void setAlipayAccount(String alipayAccount) {
            mAlipayAccount = alipayAccount;
        }

        public String getRealName() {
            return mRealName;
        }

        public void setRealName(String realName) {
            mRealName = realName;
        }

        public int getTradeNum() {
            return mTradeNum;
        }

        public void setTradeNum(int tradeNum) {
            mTradeNum = tradeNum;
        }
    }
}
