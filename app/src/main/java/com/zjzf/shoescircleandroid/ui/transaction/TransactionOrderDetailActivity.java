package com.zjzf.shoescircleandroid.ui.transaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.interfaces.SimpleCallback;
import com.zjzf.shoescircle.lib.utils.DecimalUtil;
import com.zjzf.shoescircle.lib.utils.MultiSpanUtil;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.ui.widget.textview.ExTextView;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.manager.UserInfoManager;
import com.zjzf.shoescircleandroid.model.BaseActivityOption;
import com.zjzf.shoescircleandroid.ui.im.message.TransactionMessageWrapper;

import java.util.Locale;

import butterknife.BindView;

/**
 * Created by 陈志远 on 2018/12/9.
 */
public class TransactionOrderDetailActivity extends BaseActivity {

    Option mOption;
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

    private double fee;

    @Override
    public void onHandleIntent(Intent intent) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_transaction_order_detail;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {
        UserInfoManager.INSTANCE.getUserInfo().getUserFee(new SimpleCallback<Double>() {
            @Override
            public void onCall(Double data) {
                fee = data;
            }
        });
    }

    @Override
    protected void onInitView(View decorView) {
        mOption = getActivityOption(Option.class);
        if (mOption == null) {
            finish();
            return;
        }
        String feeStr = DecimalUtil.format(fee * 100);
        MultiSpanUtil.create(R.string.transaction_rule)
                .append("本交易平台收取一定服务费。金额与会员等级有关。")
                .setTextColor(Color.parseColor("#FF9442"))
                .into(mTvRules);
        mTvFee.setText(String.format("手续费（%1$s%%）", feeStr));
        if (mOption.mMessageWrapper != null) {
            mTvName.setText(mOption.mMessageWrapper.getName());
            mTvGoodsSize.setText(String.format("尺码：%s", mOption.mMessageWrapper.getSize()));
            mTvGoodsAmount.setText(String.format(Locale.getDefault(), "数量：%d", mOption.mMessageWrapper.getNum()));
            mTvPrice.setText(StringUtil.getString(R.string.money_value, mOption.mMessageWrapper.getOrderAmount()));
            double amount = StringUtil.toDouble(mOption.mMessageWrapper.getOrderAmount());
            double feeValue = amount * (fee * 1f / 100);
            double resultValue = amount - feeValue;

            mTvFeeAmount.setText(String.format("-%s", StringUtil.getString(R.string.money_value, DecimalUtil.format(feeValue))));
            mTvSumAmount.setText(StringUtil.getString(R.string.money_value, DecimalUtil.format(resultValue)));
        }


    }

    public static class Option extends BaseActivityOption<Option> {
        private TransactionMessageWrapper mMessageWrapper;

        public Option setMessageWrapper(TransactionMessageWrapper messageWrapper) {
            mMessageWrapper = messageWrapper;
            return this;
        }
    }

}
