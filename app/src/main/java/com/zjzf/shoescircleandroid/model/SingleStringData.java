package com.zjzf.shoescircleandroid.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 陈志远 on 2018/11/8.
 * <p>
 * 单个返回的data
 */
public class SingleStringData {
    @SerializedName(value = "name", alternate = {"paymentMethodId","canAdjust", "isAdjusting", "totalAmount","orderId"})
    private String result;

    public String getResult() {
        return result;
    }
}
