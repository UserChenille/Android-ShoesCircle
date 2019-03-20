package com.zjzf.shoescircleandroid.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 陈志远 on 2018/10/15.
 */
public class UserDetailInfo implements Serializable {


    /**
     * id : 1026362234707525600
     * enable : 1
     * createTime : 2018-08-06 15:00:00
     * account : zY158571930368547
     * phone : 15857193036
     * memName : 上帝
     * avatar : http://www.baidu.com
     * sex : 1
     * evaluate : 5
     * score : 0
     * level : 1
     * xqMemlevel : {"id":1,"enable":1,"createTime":"2018-07-08 15:18:47","levelNo":1,"name":"大众会员","score":0,"fee":0.07}
     */

    @SerializedName("id")
    private String mId;
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
    @SerializedName("upgradeScore")
    private int mUpgradeScore;
    @SerializedName("level")
    private int mLevel;
    @SerializedName("xqMemlevel")
    private XqMemlevelInfo mXqMemlevel;
    @SerializedName("alipayAccount")
    private String mAliPayAccount;
    @SerializedName("realName")
    private String mRealName;
    @SerializedName("tradeNum")
    private int mTradeNum;


    public String getId() {
        return mId;
    }

    public void setId(String id) {
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

    public XqMemlevelInfo getXqMemlevel() {
        return mXqMemlevel;
    }

    public void setXqMemlevel(XqMemlevelInfo xqMemlevel) {
        mXqMemlevel = xqMemlevel;
    }

    public static class XqMemlevelInfo {
        /**
         * id : 1
         * enable : 1
         * createTime : 2018-07-08 15:18:47
         * levelNo : 1
         * name : 大众会员
         * score : 0
         * fee : 0.07
         */

        @SerializedName("id")
        private int mId;
        @SerializedName("enable")
        private int mEnable;
        @SerializedName("createTime")
        private String mCreateTime;
        @SerializedName("levelNo")
        private int mLevelNo;
        @SerializedName("name")
        private String mName;
        @SerializedName("score")
        private int mScore;
        @SerializedName("fee")
        private double mFee;

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

        public int getLevelNo() {
            return mLevelNo;
        }

        public void setLevelNo(int levelNo) {
            mLevelNo = levelNo;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public int getScore() {
            return mScore;
        }

        public void setScore(int score) {
            mScore = score;
        }

        public double getFee() {
            return mFee;
        }

        public void setFee(double fee) {
            mFee = fee;
        }

        @Override
        public String toString() {
            return "用户信息{" +
                    "mId=" + mId +
                    "\n mEnable=" + mEnable +
                    "\nmCreateTime='" + mCreateTime + '\'' +
                    "\nmLevelNo=" + mLevelNo +
                    "\nmName='" + mName + '\'' +
                    "\nmScore=" + mScore +
                    "\nmFee=" + mFee +
                    '}';
        }
    }

    public int getUpgradeScore() {
        return mUpgradeScore;
    }

    public String getAliPayAccount() {
        return mAliPayAccount;
    }

    public String getRealName() {
        return mRealName;
    }

    public int getTradeNum() {
        return mTradeNum;
    }

    @Override
    public String toString() {
        return "UserDetailInfo{" +
                "mId='" + mId + '\'' +
                "\n mEnable=" + mEnable +
                "\n mCreateTime='" + mCreateTime + '\'' +
                "\n mAccount='" + mAccount + '\'' +
                "\n mPhone='" + mPhone + '\'' +
                "\n mMemName='" + mMemName + '\'' +
                "\n mAvatar='" + mAvatar + '\'' +
                "\n mSex=" + mSex +
                "\n mEvaluate=" + mEvaluate +
                "\n mScore=" + mScore +
                "\n mUpgradeScore=" + mUpgradeScore +
                "\n mLevel=" + mLevel +
                "\n mXqMemlevel=" + String.valueOf(mXqMemlevel) +
                "\n mAliPayAccount='" + mAliPayAccount + '\'' +
                "\n mRealName='" + mRealName + '\'' +
                "\n mTradeNum=" + mTradeNum +
                '}';
    }
}
