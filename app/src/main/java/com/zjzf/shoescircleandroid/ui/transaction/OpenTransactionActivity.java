package com.zjzf.shoescircleandroid.ui.transaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.interfaces.SimpleCallback;
import com.zjzf.shoescircle.lib.manager.ImageLoaderManager;
import com.zjzf.shoescircle.lib.net.client.NetClient;
import com.zjzf.shoescircle.lib.utils.DecimalUtil;
import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircle.lib.utils.MultiSpanUtil;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.lib.utils.TextWatcherAdapter;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.lib.widget.imageview.RoundedImageView;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.ui.widget.edit.ExEditText;
import com.zjzf.shoescircle.ui.widget.textview.button.ExButton;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.manager.RongYunManager;
import com.zjzf.shoescircleandroid.base.manager.UserInfoManager;
import com.zjzf.shoescircleandroid.base.net.apis.GoodsApis;
import com.zjzf.shoescircleandroid.base.net.client.ApiException;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;
import com.zjzf.shoescircleandroid.model.BaseActivityOption;
import com.zjzf.shoescircleandroid.model.im.TransactionMessageContent;
import com.zjzf.shoescircleandroid.ui.im.message.TransactionMessage;
import com.zjzf.shoescircleandroid.widget.popup.PopupSelectSize;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by 陈志远 on 2018/12/15.
 */
public class OpenTransactionActivity extends BaseActivity {
    @BindView(R.id.iv_avatar)
    RoundedImageView mIvAvatar;
    @BindView(R.id.tv_desc)
    TextView mTvDesc;
    @BindView(R.id.ed_amount)
    ExEditText mEdAmount;
    @BindView(R.id.tv_fee_desc)
    TextView mTvFeeDesc;
    @BindView(R.id.tv_fee)
    TextView mTvFee;
    @BindView(R.id.tv_real_amount)
    TextView mTvRealAmount;
    @BindView(R.id.ed_product_name)
    ExEditText mEdProductName;
    @BindView(R.id.ed_product_no)
    ExEditText mEdProductNo;
    @BindView(R.id.tv_size)
    TextView mTvSize;
    @BindView(R.id.ed_product_count)
    ExEditText mEdProductCount;
    @BindView(R.id.tv_rules)
    TextView mTvRules;
    @BindView(R.id.btn_open_transaction)
    ExButton mBtnOpenTransaction;

    Option mOption;

    private PopupSelectSize mPopupSelectSize;
    private double fee;

    @Override
    public void onHandleIntent(Intent intent) {
        mOption = getActivityOption(Option.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_open_transaction;
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
        getTitleBarView().setLeftIcon(0);
        mPopupSelectSize = new PopupSelectSize(getContext());
        mPopupSelectSize.setOnConfirmListener(new PopupSelectSize.OnConfirmListener() {
            @Override
            public void onConfirmListener(List<String> selectedSize) {
                mTvSize.setText(TextUtils.join(",", selectedSize));
            }
        });
        TextWatcherAdapter adapter = new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                checkNextEnable();
            }
        };
        ViewUtil.setEditTextWatcher(adapter, mEdAmount, mEdProductName, mEdProductNo, mTvSize, mEdProductCount);
        TextWatcherAdapter mAmountAdapter = new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    double amount = StringUtil.toDouble(s.toString().trim());
                    double calculatedFee = amount * (fee / 100);
                    double realAmount = amount - calculatedFee;

                    mTvFee.setText(DecimalUtil.format(calculatedFee));
                    mTvRealAmount.setText(DecimalUtil.format(realAmount));
                }
            }
        };
        mEdAmount.addTextChangedListener(mAmountAdapter);
        String feeStr = DecimalUtil.format(fee * 100);
        mTvFeeDesc.setText(String.format("手续费（%1$s%%）", feeStr));
        MultiSpanUtil.create(R.string.transaction_rule)
                .append("本交易平台收取一定服务费。金额与会员等级有关。")
                .setTextColor(Color.parseColor("#FF9442"))
                .into(mTvRules);
        if (mOption != null) {
            ImageLoaderManager.INSTANCE.loadCircleImage(mIvAvatar, mOption.otherAvatar);
            mTvDesc.setText(String.format("向%s发起收款", mOption.otherName));
            mEdProductName.setText(mOption.productName);
            mEdProductNo.setText(mOption.productNo);
            List<String> sizes = mOption.sizes;
            String sizeStr = null;
            if (!ToolUtil.isListEmpty(sizes)) {
                sizeStr = TextUtils.join(",", sizes);
            }
            mTvSize.setText(sizeStr);
            mPopupSelectSize.setSelectedData(mOption.sizes);
            ViewUtil.adjustEditTextSelection(mEdProductName, mEdProductNo);
        }
    }


    private void checkNextEnable() {
        boolean enable = StringUtil.noEmpty(mEdAmount.getText(), mEdProductName.getText(), mEdProductNo.getText(), mTvSize.getText(), mEdProductCount.getText());
        mBtnOpenTransaction.setEnabled(enable);
    }

    @OnClick(R.id.layout_selecte_size)
    void selectSize() {
        mPopupSelectSize.showPopupWindow();
    }

    @OnClick(R.id.btn_open_transaction)
    void openTransaction() {
        RetrofitClient.get()
                .create(GoodsApis.class)
                .createOrder(NetClient.requestBody()
                        .put("memId", mOption.otherId)
                        .put("orderAmount", mTvRealAmount.getText().toString().trim())
                        .put("freightNo", mEdProductNo.getNonFormatText().trim())
                        .put("size", mTvSize.getText().toString())
                        .put("num", mEdProductCount.getNonFormatText().trim())
                        .put("name", mEdProductName.getNonFormatText().trim())
                        .build())
                .compose(this.<ObjectData<String>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData<String>>(getContext(), true, true) {
                    @Override
                    public void onNext(ObjectData<String> result) {
                        TransactionMessageContent messageContent = new TransactionMessageContent();
                        messageContent.setOrderAmount(mTvRealAmount.getText().toString().trim());
                        messageContent.setMemId(UserInfoManager.INSTANCE.getUserInfo().getUserDetailInfo().getId());
                        messageContent.setSize(mTvSize.getText().toString());
                        messageContent.setName(mEdProductName.getNonFormatText().trim());
                        messageContent.setFreightNo(mEdProductNo.getNonFormatText().trim());
                        messageContent.setNum(StringUtil.toInt(mEdProductCount.getNonFormatText().trim()));
                        messageContent.setOrderId(result.getData());
                        TransactionMessage message = new TransactionMessage(messageContent);
                        sendMessage(message);
                    }

                    @Override
                    public void onFailure(ApiException e, int code, String message) {
                        super.onFailure(e, code, message);
                        if (code == 40105) {
                            //跳转支付宝绑定
                            ActivityLauncher.startToBindAliPayActivity(getContext(), 40105);
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 40105 && resultCode == RESULT_OK) {
            openTransaction();
        }
    }

    private void sendMessage(TransactionMessage message) {

        RongYunManager.INSTANCE.sendMessage(RongYunManager.MessageBuilder.create()
                .conversationType(Conversation.ConversationType.PRIVATE)
                .custom()
                .setContent(message)
                .build()
                .targetId(mOption.otherId)
                .listener(new RongYunManager.SendMessageResultListener() {
                    @Override
                    public void onAttachedToDB(Message message) {

                    }

                    @Override
                    public void onSuccess(Message message) {
                        dismissLoadingDialog();
                        finish();
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        dismissLoadingDialog();
                        UIHelper.toast(message.toString());
                        LogHelper.trace(LogHelper.e, message.toString());
                    }

                    @Override
                    public void onProgress(Message message, int progress) {

                    }
                }));
    }

    public static class Option extends BaseActivityOption<Option> {
        private String otherAvatar;
        private String otherName;
        private String otherId;
        private String productName;
        private String productNo;
        private List<String> sizes;

        public Option setOtherAvatar(String otherAvatar) {
            this.otherAvatar = otherAvatar;
            return this;
        }

        public Option setOtherName(String otherName) {
            this.otherName = otherName;
            return this;
        }

        public Option setOtherId(String otherId) {
            this.otherId = otherId;
            return this;
        }

        public Option setProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public Option setProductNo(String productNo) {
            this.productNo = productNo;
            return this;
        }

        public Option setSizes(List<String> sizes) {
            this.sizes = sizes;
            return this;
        }

    }
}
