package com.zjzf.shoescircleandroid.helper.pay;

/**
 * Created by 陈志远 on 2019/3/10.
 */
public abstract class IPayHelper implements PayResultListener {
    protected PayHelper mPayHelper;
    private PayResultListener mPayResultListener;

    protected IPayHelper(PayHelper payHelper) {
        mPayHelper = payHelper;
    }

    public void pay(PayResultListener mPayResultListener) {
        this.mPayResultListener = mPayResultListener;
        onPay();
    }

    @Override
    public void onPaySuccess() {
        if (mPayResultListener != null) {
            mPayResultListener.onPaySuccess();
        }
    }

    @Override
    public void onPayError(String errorMessage) {
        if (mPayResultListener != null) {
            mPayResultListener.onPayError(errorMessage);
        }
    }

    @Override
    public void onPayCancel() {
        if (mPayResultListener != null) {
            mPayResultListener.onPayCancel();
        }
    }

    protected abstract void onPay();

}
