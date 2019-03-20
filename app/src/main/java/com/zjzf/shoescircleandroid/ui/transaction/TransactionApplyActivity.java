package com.zjzf.shoescircleandroid.ui.transaction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.interfaces.SimpleCallback;
import com.zjzf.shoescircle.lib.utils.DecimalUtil;
import com.zjzf.shoescircle.lib.utils.MultiSpanUtil;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.ui.widget.textview.button.ExButton;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.manager.UserInfoManager;
import com.zjzf.shoescircleandroid.base.net.apis.GoodsApis;
import com.zjzf.shoescircleandroid.base.net.apis.UserApis;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;
import com.zjzf.shoescircleandroid.helper.pay.PayHelper;
import com.zjzf.shoescircleandroid.helper.pay.PayResultListener;
import com.zjzf.shoescircleandroid.model.BaseActivityOption;
import com.zjzf.shoescircleandroid.model.UserDetailInfo;
import com.zjzf.shoescircleandroid.model.order.OrderDetail;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 陈志远 on 2018/12/23.
 */
public class TransactionApplyActivity extends BaseActivity {

    static final int STATE_NORMAL = 0x10;
    static final int STATE_HAS_PAY = 0x11;

    int state = 0;

    @BindView(R.id.tv_merchant)
    TextView mTvMerchant;
    @BindView(R.id.tv_amount)
    TextView mTvAmount;
    @BindView(R.id.tv_product_name)
    TextView mTvProductName;
    @BindView(R.id.tv_product_no)
    TextView mTvProductNo;
    @BindView(R.id.tv_size)
    TextView mTvSize;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.tv_create_time)
    TextView mTvCreateTime;
    @BindView(R.id.ed_address)
    EditText mEdAddress;
    @BindView(R.id.tv_rules)
    TextView mTvRules;
    @BindView(R.id.btn_pay)
    ExButton mBtnPay;
    @BindView(R.id.iv_has_payed)
    ImageView ivHasPay;
    @BindView(R.id.scroller_view)
    NestedScrollView mNestedScrollView;

    Option mOption;

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_transaction_apply;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onInitView(View decorView) {
        mOption = getActivityOption(Option.class);
        if (mOption == null) {
            finish();
            return;
        }
        UserInfoManager.INSTANCE.getUserInfo().getUserFee(new SimpleCallback<Double>() {
            @Override
            public void onCall(Double data) {
                MultiSpanUtil.create(R.string.transaction_rule)
                        .append("本交易平台收取一定服务费。金额与会员等级有关。")
                        .setTextColor(Color.parseColor("#FF9442"))
                        .into(mTvRules);
            }
        });

        RetrofitClient.get()
                .create(GoodsApis.class)
                .getOrderDetail(mOption.orderId)
                .compose(this.<ObjectData<OrderDetail>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData<OrderDetail>>() {
                    @Override
                    public void onNext(ObjectData<OrderDetail> result) {
                        OrderDetail orderDetail = result.getData();
                        if (orderDetail == null) return;
                        setState(TextUtils.isEmpty(orderDetail.getPaySn()) ? STATE_NORMAL : STATE_HAS_PAY);
                        if (StringUtil.noEmpty(orderDetail.getSeller())) {
                            mTvMerchant.setText(String.format("%s向你发起收款", orderDetail.getSeller()));
                        } else {
                            loadSeller(orderDetail.getSellerId());
                        }
                        mTvAmount.setText(DecimalUtil.format(orderDetail.getOrderAmount()));
                        mTvProductName.setText(orderDetail.getName());
                        mTvProductNo.setText(orderDetail.getFreightNo());
                        mTvSize.setText(orderDetail.getSize());
                        mEdAddress.setText(orderDetail.getAddressInfo());
                        mTvCount.setText(String.valueOf(orderDetail.getNum()));
                        mTvCreateTime.setText(orderDetail.getCreateTime());
                    }
                });
    }

    void setState(int state) {
        if (this.state == state) return;
        this.state = state;
        if (state == STATE_NORMAL) {
            mBtnPay.setEnabled(true);
            mBtnPay.setText("立即付款");
            mEdAddress.setEnabled(true);
        } else {
            mBtnPay.setEnabled(false);
            mBtnPay.setText("已付款");
            mEdAddress.setEnabled(false);
            playSuccessAnimate();
        }
    }

    private void loadSeller(String sellerId) {
        RetrofitClient.get()
                .create(UserApis.class)
                .getUserById(sellerId)
                .compose(this.<ObjectData<UserDetailInfo>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData<UserDetailInfo>>() {
                    @Override
                    public void onNext(ObjectData<UserDetailInfo> result) {
                        if (result.getData() == null) return;
                        UserDetailInfo detailInfo = result.getData();
                        mTvMerchant.setText(String.format("%s向你发起收款", detailInfo.getMemName()));
                    }
                });
    }

    @OnClick(R.id.btn_pay)
    void pay() {
        if (TextUtils.isEmpty(mEdAddress.getText().toString().trim())) {
            UIHelper.toast("请完善收件人信息");
            return;
        }
        UserInfoManager.INSTANCE.getUserInfo()
                .getUserDetailInfo(new SimpleCallback<UserDetailInfo>() {
                    @Override
                    public void onCall(UserDetailInfo data) {
                        if (TextUtils.isEmpty(data.getAccount())) {
                            //支付宝认证
                            ActivityLauncher.startToBindAliPayActivity(getContext(), 1001);
                        } else {
                            doPay();
                        }
                    }
                }, true);
    }

    private void doPay() {
        PayHelper.with(this)
                .aliPay()
                .setOrderId(mOption.orderId)
                .setAddressInfo(mEdAddress.getText().toString().trim())
                .pay(new PayResultListener() {
                    @Override
                    public void onPaySuccess() {
                        UIHelper.toast("支付成功");
                        playSuccessAnimate();
                        setState(STATE_HAS_PAY);
                    }

                    @Override
                    public void onPayError(String errorMessage) {
                        UIHelper.toast(errorMessage);
                    }

                    @Override
                    public void onPayCancel() {
                        UIHelper.toast("用户取消支付");
                    }
                });
    }

    private void playSuccessAnimate() {
        mNestedScrollView.smoothScrollTo(0, 0);
//        mNestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
        ivHasPay.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivHasPay.setScaleX(20f);
                ivHasPay.setScaleY(20f);
                ivHasPay.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                ViewUtil.shakeView(findViewById(android.R.id.content), 200);
                                super.onAnimationEnd(animation);
                            }
                        })
                        .start();
            }
        }, 300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1001) {
            pay();
        }
    }

    @Override
    public void finish() {
        if (state == STATE_HAS_PAY) {
            setResult(RESULT_OK);
        }
        super.finish();
    }

    public static class Option extends BaseActivityOption<Option> {
        private String orderId;

        public Option setOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }
    }
}
