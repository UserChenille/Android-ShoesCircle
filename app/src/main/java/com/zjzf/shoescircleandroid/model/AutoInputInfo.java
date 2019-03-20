package com.zjzf.shoescircleandroid.model;

import com.google.gson.annotations.SerializedName;
import com.zjzf.shoescircleandroid.base.api.IObjectTag;

/**
 * Created by 陈志远 on 2018/10/17.
 */
public class AutoInputInfo implements IObjectTag<String> {
    private int type;

    /**
     * id : 1035425177487204400
     * enable : 1
     * createTime : 2018-08-31 15:12:54
     * memId : 1026362234707525600
     * member : null
     * name : 贝壳头
     * photo : tfhf23121312
     * freightNo : c77124
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
    private long mMemId;
    @SerializedName("member")
    private Object mMember;
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

    public AutoInputInfo setType(int type) {
        this.type = type;
        return this;
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

    public long getMemId() {
        return mMemId;
    }

    public void setMemId(long memId) {
        mMemId = memId;
    }

    public Object getMember() {
        return mMember;
    }

    public void setMember(Object member) {
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

    @Override
    public String get() {
        return type == 0 ? mName : mFreightNo;
    }
}
