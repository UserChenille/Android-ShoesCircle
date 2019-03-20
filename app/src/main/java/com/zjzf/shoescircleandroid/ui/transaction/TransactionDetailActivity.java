package com.zjzf.shoescircleandroid.ui.transaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.interfaces.SimpleCallback;
import com.zjzf.shoescircle.lib.utils.DecimalUtil;
import com.zjzf.shoescircle.lib.utils.MultiSpanUtil;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.lib.utils.TimeUtil;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.ui.widget.textview.ExTextView;
import com.zjzf.shoescircle.ui.widget.textview.button.ExButton;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.manager.UserInfoManager;
import com.zjzf.shoescircleandroid.base.net.apis.ExpressApis;
import com.zjzf.shoescircleandroid.base.net.apis.GoodsApis;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;
import com.zjzf.shoescircleandroid.model.BaseActivityOption;
import com.zjzf.shoescircleandroid.model.express.ExpressInfo;
import com.zjzf.shoescircleandroid.model.order.OrderDetail;
import com.zjzf.shoescircleandroid.ui.express.ExpressDetailActivity;
import com.zjzf.shoescircleandroid.ui.transaction.scan.QRScanActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 陈志远 on 2018/12/9.
 */
public class TransactionDetailActivity extends BaseActivity {
    static final int STATE_WAIT_PAY = 10;
    static final int STATE_HAS_PAY = 20;
    static final int STATE_HAS_SEND = 30;
    static final int STATE_COMPELETE = 40;

    int state = 0;

    @BindView(R.id.tv_time_countdown)
    TextView mTvTimeCountdown;
    @BindView(R.id.container_consignee)
    LinearLayout mContainerConsignee;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_goods_size)
    TextView mTvGoodsSize;
    @BindView(R.id.tv_goods_amount)
    TextView mTvGoodsAmount;
    @BindView(R.id.tv_price)
    ExTextView mTvPrice;
    @BindView(R.id.tv_fee)
    TextView mTvFee;
    @BindView(R.id.tv_fee_amount)
    ExTextView mTvFeeAmount;
    @BindView(R.id.tv_sum_amount)
    ExTextView mTvSumAmount;
    @BindView(R.id.tv_rules)
    TextView mTvRules;
    @BindView(R.id.btn_to_ship)
    ExButton mBtnToShip;
    @BindView(R.id.tv_state_title)
    ExTextView mTvStateTitle;
    @BindView(R.id.tv_state_desc)
    ExTextView mTvStateDesc;
    @BindView(R.id.tv_state_desc2)
    ExTextView mTvStateDesc2;
    @BindView(R.id.layout_desc)
    LinearLayout mLayoutDesc;
    @BindView(R.id.iv_arrow)
    ImageView mIvArrow;
    @BindView(R.id.layout_title)
    LinearLayout mLayoutTitle;
    @BindView(R.id.tv_address)
    ExTextView mTvAddress;

    Option mOption;
    private CountDownTimer mCountDownTimer;

    @Override
    public void onHandleIntent(Intent intent) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_transaction_detail;
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
        MultiSpanUtil.create(R.string.transaction_rule)
                .append("本交易平台收取一定服务费。金额与会员等级有关。")
                .setTextColor(Color.parseColor("#FF9442"))
                .into(mTvRules);
        RetrofitClient.get()
                .create(GoodsApis.class)
                .getOrderDetail(mOption.orderId)
                .compose(this.<ObjectData<OrderDetail>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData<OrderDetail>>() {
                    @Override
                    public void onNext(ObjectData<OrderDetail> result) {
                        final OrderDetail orderDetail = result.getData();
                        if (orderDetail == null) return;
                        setState(orderDetail);
                        mTvName.setText(orderDetail.getName());
                        mTvGoodsSize.setText(String.format("尺码：%1$s", orderDetail.getSize()));
                        mTvGoodsAmount.setText(String.format("数量：%1$s", orderDetail.getNum()));
                        mTvPrice.setText(StringUtil.getString(R.string.money_value, DecimalUtil.format(orderDetail.getOrderAmount())));
                        UserInfoManager.INSTANCE.getUserInfo().getUserFee(new SimpleCallback<Double>() {
                            @Override
                            public void onCall(Double data) {
                                String originFee = DecimalUtil.format(data * 100) + "%";
                                String curFee = DecimalUtil.format(orderDetail.getFee() * 100) + "%";
                                if (orderDetail.getFee() < data) {
                                    String resource = String.format("手续费（%1$s %2$S）", curFee, originFee);
                                    MultiSpanUtil.create(resource)
                                            .append(originFee).setDeleteLine(true).setTextColorFromRes(R.color.color_black2)
                                            .into(mTvFee);
                                } else {
                                    mTvFee.setText(String.format("手续费（%1$s）", curFee));
                                }
                            }
                        });
                        mTvFeeAmount.setText(String.format("-%s", StringUtil.getString(R.string.money_value, orderDetail.getRewardAmount())));
                        mTvSumAmount.setText(StringUtil.getString(R.string.money_value, DecimalUtil.format(orderDetail.getOrderAmount() - orderDetail.getRewardAmount())));
                    }
                });


    }

    OrderDetail mOrderDetail;

    private void setState(OrderDetail orderDetail) {
        if (this.state == orderDetail.getOrderState()) return;
        this.state = orderDetail.getOrderState();
        this.mOrderDetail = orderDetail;
        switch (state) {
            case STATE_WAIT_PAY:
                ViewUtil.setViewsVisible(View.GONE, mLayoutDesc, mIvArrow, mContainerConsignee);
                mTvStateTitle.setText("求货人未付款");
                mTvStateDesc.setText("请等待...");
                mTvAddress.setText(orderDetail.getAddressInfo());
                break;
            case STATE_HAS_PAY:
                ViewUtil.setViewsVisible(View.VISIBLE, mContainerConsignee, mLayoutDesc);
                ViewUtil.setViewsVisible(View.GONE, mIvArrow);
                mTvStateTitle.setText("求货人已付款");
                mTvStateDesc.setText("请在规定时间内发货");
                mTvStateDesc2.setText("剩余时间");
                startCountDown(orderDetail.getRemainingDeliverySeconds());
                break;
            case STATE_HAS_SEND:
                ViewUtil.setViewsVisible(View.GONE, mLayoutDesc);
                ViewUtil.setViewsVisible(View.VISIBLE, mContainerConsignee, mIvArrow);
                mTvStateTitle.setLoadingText("正在查询...");
                mTvStateDesc.setLoadingText("正在查询...");
                queryExpress(orderDetail.getShippingCode(), orderDetail.getExpressId());
                break;
            case STATE_COMPELETE:
                ViewUtil.setViewsVisible(View.GONE, mLayoutDesc);
                ViewUtil.setViewsVisible(View.VISIBLE, mContainerConsignee, mIvArrow);
                mTvStateTitle.setText("已完成， 感谢你使用鞋圈");
                mTvStateDesc.setText(orderDetail.getFinishedTime());
                break;

        }
    }

    @OnClick(R.id.layout_title)
    void onContentClick() {
        if (mOrderDetail == null) return;
        if (state == STATE_HAS_SEND || state == STATE_COMPELETE) {
            ActivityLauncher.startToExpressDetailActivity(this, new ExpressDetailActivity.Option()
                    .setExpressCode(mOrderDetail.getShippingCode())
                    .setExpressId(mOrderDetail.getExpressId()));
        }
    }

    private void queryExpress(String shippingCode, String expressId) {
        RetrofitClient.get()
                .create(ExpressApis.class)
                .queryExpress(shippingCode, expressId)
                .compose(this.<ObjectData<List<ExpressInfo>>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData<List<ExpressInfo>>>() {
                    @Override
                    public void onNext(ObjectData<List<ExpressInfo>> result) {
                        if (!ToolUtil.isListEmpty(result.getData())) {
                            ExpressInfo info = result.getData().get(0);
                            mTvStateTitle.setText(info.getStatusDescription());
                            mTvStateDesc.setText(info.getDate());
                        } else {
                            mTvStateTitle.setText("暂无物流信息");
                            mTvStateDesc.setText(" ");
                        }
                    }
                });
    }

    private void startCountDown(int remainingDeliverySeconds) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        if (remainingDeliverySeconds > 0) {
            remainingDeliverySeconds = remainingDeliverySeconds * 1000;
            updateCountDownUI(remainingDeliverySeconds, false);
            mCountDownTimer = new CountDownTimer(remainingDeliverySeconds, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    updateCountDownUI(millisUntilFinished, false);
                }

                @Override
                public void onFinish() {
                    updateCountDownUI(0, true);
                }
            };
            mCountDownTimer.start();
        } else {
            updateCountDownUI(0, true);
        }

    }

    private void updateCountDownUI(long time, boolean finish) {
        if (finish) {
            mTvTimeCountdown.setText("0天0小时0分钟0秒");
            return;
        }

        long diff = time / 1000;
        long day = diff / TimeUtil.DAY;
        long hour = diff % TimeUtil.DAY / TimeUtil.HOUR;
        long min = diff % TimeUtil.DAY % TimeUtil.HOUR / 60;
        long second = diff % TimeUtil.DAY % TimeUtil.HOUR % TimeUtil.MINUTE;

        mTvTimeCountdown.setText(String.format("%1$s天%2$s小时%3$s分%4$s秒", day, hour, min, second));

    }

    @OnClick(R.id.btn_to_ship)
    void toShip() {
        ActivityLauncher.startToQRCodeScanActivity(this, new QRScanActivity.Option().setOrderId(mOption.orderId));
    }

    @Override
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        super.onDestroy();
    }


    public static class Option extends BaseActivityOption<Option> {
        private String orderId;

        public Option setOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }
    }

}
