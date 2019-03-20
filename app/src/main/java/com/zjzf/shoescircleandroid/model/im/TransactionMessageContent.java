package com.zjzf.shoescircleandroid.model.im;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 陈志远 on 2018/12/15.
 */
public class TransactionMessageContent implements Parcelable {

    /**
     * orderAmount : 2500
     * memId : 1052218251554304002
     * size : 37.5,39,40.5
     * num : 1
     * orderId : 0
     * freightNo : aa123
     * name : 测试1
     */

    //订单金额
    private String orderAmount;
    //付款人id
    private String memId;
    //尺寸
    private String size;
    //数量
    private int num;
    //订单号
    private String orderId;
    //货号
    private String freightNo;
    //货品名字
    private String name;
    //地址信息
    private String addressInfo;


    public TransactionMessageContent() {
    }

    protected TransactionMessageContent(Parcel in) {
        orderAmount = in.readString();
        memId = in.readString();
        size = in.readString();
        num = in.readInt();
        orderId = in.readString();
        freightNo = in.readString();
        name = in.readString();
        addressInfo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderAmount);
        dest.writeString(memId);
        dest.writeString(size);
        dest.writeInt(num);
        dest.writeString(orderId);
        dest.writeString(freightNo);
        dest.writeString(name);
        dest.writeString(addressInfo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionMessageContent> CREATOR = new Creator<TransactionMessageContent>() {
        @Override
        public TransactionMessageContent createFromParcel(Parcel in) {
            return new TransactionMessageContent(in);
        }

        @Override
        public TransactionMessageContent[] newArray(int size) {
            return new TransactionMessageContent[size];
        }
    };

    public String getOrderAmount() {
        return orderAmount;
    }

    public TransactionMessageContent setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
        return this;
    }

    public String getMemId() {
        return memId;
    }

    public TransactionMessageContent setMemId(String memId) {
        this.memId = memId;
        return this;
    }

    public String getSize() {
        return size;
    }

    public TransactionMessageContent setSize(String size) {
        this.size = size;
        return this;
    }

    public int getNum() {
        return num;
    }

    public TransactionMessageContent setNum(int num) {
        this.num = num;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public TransactionMessageContent setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getFreightNo() {
        return freightNo;
    }

    public TransactionMessageContent setFreightNo(String freightNo) {
        this.freightNo = freightNo;
        return this;
    }

    public String getName() {
        return name;
    }

    public TransactionMessageContent setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddressInfo() {
        return addressInfo;
    }

    public TransactionMessageContent setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
        return this;
    }
}
