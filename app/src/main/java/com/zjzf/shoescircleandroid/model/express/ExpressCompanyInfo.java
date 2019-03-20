package com.zjzf.shoescircleandroid.model.express;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.google.gson.annotations.SerializedName;
import com.zjzf.shoescircle.lib.base.baseadapter.MultiType;
import com.zjzf.shoescircle.lib.utils.StringUtil;

/**
 * Created by 陈志远 on 2019/3/11.
 */
public class ExpressCompanyInfo implements MultiType, Parcelable {

    /**
     * code : china-post
     * phone : null
     * name : China Post
     * type : null
     * name_cn : 中国邮政
     * picture : null
     * homepage : null
     */

    @SerializedName("code")
    private String mCode;
    @SerializedName("phone")
    private String mPhone;
    @SerializedName("name")
    private String mName;
    @SerializedName("type")
    private String mType;
    @SerializedName("name_cn")
    private String mNameCn;
    @SerializedName("picture")
    private String mPicture;
    @SerializedName("homepage")
    private String mHomepage;

    private String mSortLetter;

    public ExpressCompanyInfo() {
    }

    protected ExpressCompanyInfo(Parcel in) {
        mCode = in.readString();
        mPhone = in.readString();
        mName = in.readString();
        mType = in.readString();
        mNameCn = in.readString();
        mPicture = in.readString();
        mHomepage = in.readString();
        mSortLetter = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCode);
        dest.writeString(mPhone);
        dest.writeString(mName);
        dest.writeString(mType);
        dest.writeString(mNameCn);
        dest.writeString(mPicture);
        dest.writeString(mHomepage);
        dest.writeString(mSortLetter);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExpressCompanyInfo> CREATOR = new Creator<ExpressCompanyInfo>() {
        @Override
        public ExpressCompanyInfo createFromParcel(Parcel in) {
            return new ExpressCompanyInfo(in);
        }

        @Override
        public ExpressCompanyInfo[] newArray(int size) {
            return new ExpressCompanyInfo[size];
        }
    };

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getNameCn() {
        return mNameCn;
    }

    public void setNameCn(String nameCn) {
        mNameCn = nameCn;
    }

    public String getPicture() {
        if (StringUtil.noEmpty(mPicture)) {
            if (!URLUtil.isHttpUrl(mPicture) || !URLUtil.isHttpsUrl(mPicture)) {
                return "http:" + mPicture;
            }
        }
        return mPicture;
    }

    public void setPicture(String picture) {
        mPicture = picture;
    }

    public String getHomepage() {
        return mHomepage;
    }

    public void setHomepage(String homepage) {
        mHomepage = homepage;
    }

    public ExpressCompanyInfo setSortLetter(String sortLetter) {
        mSortLetter = sortLetter;
        return this;
    }

    public String getSortLetter() {
        return mSortLetter == null ? "" : mSortLetter;
    }

    @Override
    public int getItemType() {
        return TextUtils.isEmpty(mSortLetter) ? 0 : 1;
    }
}
