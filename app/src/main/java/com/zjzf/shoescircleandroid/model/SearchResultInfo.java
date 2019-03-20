package com.zjzf.shoescircleandroid.model;

import com.google.gson.annotations.SerializedName;
import com.zjzf.shoescircle.lib.base.baseadapter.MultiType;

/**
 * Created by 陈志远 on 2018/10/16.
 */
public class SearchResultInfo implements MultiType {


    /**
     * id : 1015889815853224000
     * enable : 1
     * createTime : 2018-07-08 09:26:21
     * memId : 1
     * member : {"id":1,"enable":1,"createTime":"2018-07-08 08:46:54","account":"hehaoyang","phone":"15857193035","memName":"何昊阳","avatar":"","sex":0,"evaluate":5,"score":0,"level":1}
     * name : 乔丹
     * photo : asdasd
     * freightNo : AA223AA
     * size : 41,42
     * price : 111
     * state : 1
     */

    @SerializedName("id")
    private long mId;
    @SerializedName("enable")
    private int mEnable;
    @SerializedName("createTime")
    private String mCreateTime;
    @SerializedName("memId")
    private int mMemId;
    @SerializedName("member")
    private MemberInfo mMember;
    @SerializedName("name")
    private String mName;
    @SerializedName("photo")
    private String mPhoto;
    @SerializedName("freightNo")
    private String mFreightNo;
    @SerializedName("size")
    private String mSize;
    @SerializedName("price")
    private int mPrice;
    @SerializedName("state")
    private int mState;

    @Override
    public int getItemType() {
        return 0;
    }

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

    public int getMemId() {
        return mMemId;
    }

    public void setMemId(int memId) {
        mMemId = memId;
    }

    public MemberInfo getMember() {
        return mMember;
    }

    public void setMember(MemberInfo member) {
        mMember = member;
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

    public String getFreightNo() {
        return mFreightNo;
    }

    public void setFreightNo(String freightNo) {
        mFreightNo = freightNo;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        mSize = size;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }

    public static class MemberInfo {
        /**
         * id : 1
         * enable : 1
         * createTime : 2018-07-08 08:46:54
         * account : hehaoyang
         * phone : 15857193035
         * memName : 何昊阳
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
