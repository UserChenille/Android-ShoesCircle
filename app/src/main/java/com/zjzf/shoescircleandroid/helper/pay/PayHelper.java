package com.zjzf.shoescircleandroid.helper.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.zjzf.shoescircle.lib.helper.PermissionHelper;
import com.zjzf.shoescircle.lib.helper.RxHelper;
import com.zjzf.shoescircle.lib.interfaces.IPermission;
import com.zjzf.shoescircle.lib.interfaces.OnPermissionGrantListener;
import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircleandroid.base.net.apis.PayApis;
import com.zjzf.shoescircleandroid.base.net.client.ApiException;
import com.zjzf.shoescircleandroid.base.net.client.HandleServerApiErrorAction;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.model.pay.AliPayResult;

import java.lang.ref.WeakReference;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 陈志远 on 2019/3/10.
 */
public class PayHelper {

    private WeakReference<Activity> mWeakHandler;

    private PayHelper(Activity handler) {
        mWeakHandler = new WeakReference<>(handler);
    }


    public static PayHelper with(Activity activity) {
        return new PayHelper(activity);
    }

    Activity getActivity() {
        return mWeakHandler.get();
    }

    public AliPayHelper aliPay() {
        return new AliPayHelper(this);
    }


    public static class AliPayHelper extends IPayHelper {
        private String orderId;
        private String addressInfo;


        protected AliPayHelper(PayHelper payHelper) {
            super(payHelper);
        }


        public AliPayHelper setOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public AliPayHelper setAddressInfo(String addressInfo) {
            this.addressInfo = addressInfo;
            return this;
        }

        @Override
        protected void onPay() {
            if (mPayHelper.mWeakHandler.get() instanceof IPermission) {
                IPermission iPermission = (IPermission) mPayHelper.mWeakHandler.get();

                iPermission.getPermissionHelper().requestPermission(new OnPermissionGrantListener() {
                    @Override
                    public void onPermissionGranted(PermissionHelper.Permission... grantedPermissions) {
                        doPay();
                    }

                    @Override
                    public void onPermissionsDenied(PermissionHelper.Permission... deniedPermissions) {
                        onPayError("支付前请允许权限");
                    }
                }, PermissionHelper.Permission.WRITE_EXTERNAL_STORAGE, PermissionHelper.Permission.READ_PHONE_STATE);
            } else {
                LogHelper.trace(LogHelper.e, "非IPermission实现类，请务必自行处理权限");
                doPay();
            }

        }

        private void doPay() {
            RetrofitClient.get()
                    .create(PayApis.class)
                    .requestAliPay(orderId, addressInfo)
                    .doOnNext(new HandleServerApiErrorAction<>())
                    .compose(RxHelper.<ObjectData<String>>io_main())
                    .subscribe(new OnResponseListener<ObjectData<String>>() {
                        @Override
                        public void onNext(ObjectData<String> result) {
                            if (TextUtils.isEmpty(result.getData())) {
                                onPayError("支付订单为空");
                            } else {
                                toAliPay(result.getData());
                            }
                        }

                        @Override
                        public void onFailure(ApiException e, int code, String message) {
                            super.onFailure(e, code, message);
                            onPayError(message);
                        }
                    });
        }

        @SuppressLint("CheckResult")
        private void toAliPay(final String orderInfo) {
            Observable.create(new ObservableOnSubscribe<Map<String, String>>() {
                @Override
                public void subscribe(ObservableEmitter<Map<String, String>> emitter) throws Exception {
                    PayTask alipay = new PayTask(mPayHelper.getActivity());
                    Map<String, String> result = alipay.payV2(orderInfo, true);
                    emitter.onNext(result);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Map<String, String>>() {
                        @Override
                        public void accept(Map<String, String> result) throws Exception {
                            AliPayResult payResult = new AliPayResult(result);

                            if (payResult.isSuccessed()) {
                                onPaySuccess();
                            } else if (payResult.isError()) {
                                onPayError(payResult.getMessage());
                            } else if (payResult.isCanceled()) {
                                onPayCancel();
                            }
                        }
                    });
        }
    }
}
