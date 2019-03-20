package com.zjzf.shoescircleandroid.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈志远 on 2018/10/17.
 */
public class GoodsDetailInfo implements Parcelable {

    /**
     * id : 1015889815853224000
     * enable : 1
     * createTime : 2018-07-08 09:29:37
     * memId : 1
     * member : {"id":1,"enable":1,"createTime":"2018-07-08 08:46:54","account":"hehaoyang","phone":"15857193035","memName":"何昊阳","avatar":"","sex":0,"evaluate":5,"score":0,"level":1}
     * name : 乔丹6
     * photo : tfhf23121312
     * freightNo : AA223AA5
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

    protected GoodsDetailInfo(Parcel in) {
        mId = in.readLong();
        mEnable = in.readInt();
        mCreateTime = in.readString();
        mMemId = in.readInt();
        mMember = in.readParcelable(MemberInfo.class.getClassLoader());
        mName = in.readString();
        mPhoto = in.readString();
        mFreightNo = in.readString();
        mSize = in.readString();
        mPrice = in.readInt();
        mState = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeInt(mEnable);
        dest.writeString(mCreateTime);
        dest.writeInt(mMemId);
        dest.writeParcelable(mMember, flags);
        dest.writeString(mName);
        dest.writeString(mPhoto);
        dest.writeString(mFreightNo);
        dest.writeString(mSize);
        dest.writeInt(mPrice);
        dest.writeInt(mState);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GoodsDetailInfo> CREATOR = new Creator<GoodsDetailInfo>() {
        @Override
        public GoodsDetailInfo createFromParcel(Parcel in) {
            return new GoodsDetailInfo(in);
        }

        @Override
        public GoodsDetailInfo[] newArray(int size) {
            return new GoodsDetailInfo[size];
        }
    };

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

    public List<String> getSizeList() {
        List<String> result = new ArrayList<>();
        if (!TextUtils.isEmpty(mSize)) {
            String[] sizes = mSize.split(",");
            for (String size : sizes) {
                result.add(size);
            }
        }
        return result;
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

    public static class MemberInfo implements Parcelable{
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
        @SerializedName("level")
        private int mLevel;
        @SerializedName("tradeNum")
        private String tradeNum;

        protected MemberInfo(Parcel in) {
            mId = in.readString();
            mEnable = in.readInt();
            mCreateTime = in.readString();
            mAccount = in.readString();
            mPhone = in.readString();
            mMemName = in.readString();
            mAvatar = in.readString();
            mSex = in.readInt();
            mEvaluate = in.readInt();
            mScore = in.readInt();
            mLevel = in.readInt();
            tradeNum = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mId);
            dest.writeInt(mEnable);
            dest.writeString(mCreateTime);
            dest.writeString(mAccount);
            dest.writeString(mPhone);
            dest.writeString(mMemName);
            dest.writeString(mAvatar);
            dest.writeInt(mSex);
            dest.writeInt(mEvaluate);
            dest.writeInt(mScore);
            dest.writeInt(mLevel);
            dest.writeString(tradeNum);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<MemberInfo> CREATOR = new Creator<MemberInfo>() {
            @Override
            public MemberInfo createFromParcel(Parcel in) {
                return new MemberInfo(in);
            }

            @Override
            public MemberInfo[] newArray(int size) {
                return new MemberInfo[size];
            }
        };

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

        public String getTradeNum() {
            return tradeNum == null ? "" : tradeNum;
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
