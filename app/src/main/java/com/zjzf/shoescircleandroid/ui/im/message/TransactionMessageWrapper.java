package com.zjzf.shoescircleandroid.ui.im.message;

import com.zjzf.shoescircleandroid.model.im.TransactionMessageContent;

import java.io.Serializable;

/**
 * Created by 陈志远 on 2018/12/9.
 */
public class TransactionMessageWrapper implements Serializable {
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


    private TransactionMessageWrapper(TransactionMessageContent message) {
        if (message != null) {
            setOrderAmount(message.getOrderAmount())
                    .setMemId(message.getMemId())
                    .setSize(message.getSize())
                    .setNum(message.getNum())
                    .setOrderId(message.getOrderId())
                    .setFreightNo(message.getFreightNo())
                    .setName(message.getName())
                    .setAddressInfo(message.getAddressInfo());
        }

    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public TransactionMessageWrapper setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
        return this;
    }

    public String getMemId() {
        return memId;
    }

    public TransactionMessageWrapper setMemId(String memId) {
        this.memId = memId;
        return this;
    }

    public String getSize() {
        return size;
    }

    public TransactionMessageWrapper setSize(String size) {
        this.size = size;
        return this;
    }

    public int getNum() {
        return num;
    }

    public TransactionMessageWrapper setNum(int num) {
        this.num = num;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public TransactionMessageWrapper setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getFreightNo() {
        return freightNo;
    }

    public TransactionMessageWrapper setFreightNo(String freightNo) {
        this.freightNo = freightNo;
        return this;
    }

    public String getName() {
        return name;
    }

    public TransactionMessageWrapper setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddressInfo() {
        return addressInfo;
    }

    public TransactionMessageWrapper setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
        return this;
    }

    public static TransactionMessageWrapper createFrom(TransactionMessageContent message) {
        return new TransactionMessageWrapper(message);
    }
}
