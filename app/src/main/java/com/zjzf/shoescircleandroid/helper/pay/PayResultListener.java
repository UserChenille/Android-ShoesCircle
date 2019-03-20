package com.zjzf.shoescircleandroid.helper.pay;

/**
 * Created by 陈志远 on 2019/3/10.
 */
public interface PayResultListener {

    void onPaySuccess();

    void onPayError(String errorMessage);

    void onPayCancel();
}
