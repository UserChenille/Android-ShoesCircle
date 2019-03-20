package com.zjzf.shoescircleandroid.ui.transaction.scan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.helper.RxHelper;
import com.zjzf.shoescircle.lib.helper.rx.base.RxCall;
import com.zjzf.shoescircle.lib.utils.KeyBoardUtil;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.lib.utils.TextWatcherAdapter;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.ui.widget.edit.ExEditText;
import com.zjzf.shoescircle.ui.widget.textview.ExTextView;
import com.zjzf.shoescircle.ui.widget.textview.button.ExButton;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.net.apis.ExpressApis;
import com.zjzf.shoescircleandroid.base.net.apis.GoodsApis;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;
import com.zjzf.shoescircleandroid.model.BaseActivityOption;
import com.zjzf.shoescircleandroid.model.express.ExpressCompanyInfo;
import com.zjzf.shoescircleandroid.widget.popup.PopupRecommendExpress;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by 陈志远 on 2018/12/9.
 */
public class QRScanResultOrEditActivity extends BaseActivity {

    @BindView(R.id.iv_qr_result)
    ImageView mIvQrResult;
    @BindView(R.id.tv_ship_order)
    ExTextView mTvShipOrder;
    @BindView(R.id.tv_ship_company)
    ExTextView mTvShipCompany;
    @BindView(R.id.layout_ship_company)
    LinearLayout mLayoutShipCompany;
    @BindView(R.id.layout_auto_scan_result)
    LinearLayout mLayoutAutoScanResult;
    @BindView(R.id.ed_input_ship_num)
    ExEditText mEdInputShipNum;
    @BindView(R.id.tv_select_ship_company)
    TextView mTvSelectShipCompany;
    @BindView(R.id.layout_input_scan_result)
    LinearLayout mLayoutInputScanResult;
    @BindView(R.id.btn_sure_to_ship)
    ExButton mBtnSureToShip;
    private Option mOption;

    private PopupRecommendExpress mPopupRecommendExpress;

    private boolean isEditMode = false;
    private ExpressCompanyInfo mSelectedExpressCompanyInfo;

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_qr_scan_result_or_edit;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {
        mOption = getActivityOption(Option.class);
        if (TextUtils.isEmpty(mOption.orderId)) {
            UIHelper.toast("订单号为空");
            finish();
            return;
        }
    }

    @Override
    protected void onInitView(View decorView) {
        if (TextUtils.isEmpty(mOption.orderId)) {
            UIHelper.toast("订单号为空");
            finish();
            return;
        }
        mEdInputShipNum.addTextChangedListener(new TextWatchAdapter(mEdInputShipNum));
        mTvShipOrder.addTextChangedListener(new TextWatchAdapter(mTvShipOrder));
        if (mOption != null && StringUtil.noEmpty(mOption.mQRCode)) {
            changeMode(mOption.isEditMode);
            bindData(mOption);
        } else {
            changeMode(true);
        }
    }


    private class TextWatchAdapter extends TextWatcherAdapter {
        private StringBuilder sb = new StringBuilder();
        TextView mTarget;

        public TextWatchAdapter(TextView target) {
            mTarget = target;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s == null || s.length() == 0) {
                return;
            }
            try {
                sb.replace(0, sb.length(), "");
                for (int i = 0; i < s.length(); i++) {
                    if (i != 3 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() != 0 && sb.length() % 4 == 0) && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }

                    mTarget.setText(sb.toString());
                    if (mTarget instanceof EditText) {
                        ((EditText) mTarget).setSelection(index);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                mTarget.setText(sb.toString());
            }
        }
    }

    private void bindData(@NonNull final Option option) {
        mTvShipOrder.setText(option.mQRCode);
        if (StringUtil.noEmpty(option.mQRCode)) {
            queryExpressCompany(option.mQRCode);
        }
        createQRCode(option.mQRCode);
    }

    private void queryExpressCompany(String qrCode) {
        RetrofitClient.get()
                .create(ExpressApis.class)
                .queryExpressCompany(qrCode)
                .compose(this.<ObjectData<List<ExpressCompanyInfo>>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData<List<ExpressCompanyInfo>>>() {
                    @Override
                    public void onNext(ObjectData<List<ExpressCompanyInfo>> result) {
                        if (!ToolUtil.isListEmpty(result.getData())) {
                            showExpressPopup(result.getData());
                        }
                    }
                });
    }

    private void showExpressPopup(List<ExpressCompanyInfo> mDatas) {
        if (mPopupRecommendExpress == null) {
            mPopupRecommendExpress = new PopupRecommendExpress(this);
            mPopupRecommendExpress.setOnItemSelectListener(new PopupRecommendExpress.OnItemSelectListener() {
                @Override
                public void onItemSelected(ExpressCompanyInfo companyInfo) {
                    onSelectedCompany(companyInfo);
                }
            });
        }
        mPopupRecommendExpress.showPopupWindow(mDatas);

    }

    private void onSelectedCompany(ExpressCompanyInfo companyInfo) {
        mSelectedExpressCompanyInfo = companyInfo;
        if (companyInfo != null) {
            mTvShipCompany.setText(companyInfo.getNameCn());
            mTvSelectShipCompany.setText(companyInfo.getNameCn());
        } else {
            mTvShipCompany.setText("请选择快递公司");
            mTvSelectShipCompany.setText("");
        }
    }

    @SuppressLint("CheckResult")
    private void createQRCode(final String qrCode) {
        if (TextUtils.isEmpty(qrCode)) {
            mIvQrResult.setImageResource(R.drawable.ic_default_qr_result);
            return;
        }
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                int width = UIHelper.dip2px(313);
                int height = UIHelper.dip2px(110);
                Bitmap result = QRCodeEncoder.syncEncodeBarcode(qrCode, width, height, 0);
                if (result != null) {
                    emitter.onNext(result);
                } else {
                    emitter.onError(new NullPointerException("没有图片"));
                }
            }
        }).compose(RxHelper.<Bitmap>io_main())
                .onErrorReturn(new Function<Throwable, Bitmap>() {
                    @Override
                    public Bitmap apply(Throwable throwable) throws Exception {
                        mIvQrResult.setImageResource(R.drawable.ic_default_qr_result);
                        return null;
                    }
                })
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        mIvQrResult.setImageBitmap(bitmap);
                    }
                });
    }

    private void changeMode(boolean isEdit) {
        isEditMode = isEdit;
        ViewUtil.setViewsVisible(isEdit ? View.GONE : View.VISIBLE, mLayoutAutoScanResult);
        ViewUtil.setViewsVisible(isEdit ? View.VISIBLE : View.GONE, mLayoutInputScanResult);
        if (isEdit) {
            RxHelper.debounceListenEdittext(mEdInputShipNum, 800, new RxCall<String>() {
                @Override
                public void onCall(String data) {
                    createQRCode(data.replace(" ", ""));
                }
            });
        }
    }

    @OnClick(R.id.layout_input)
    void input() {
        KeyBoardUtil.open(mEdInputShipNum);
    }

    @OnClick({R.id.tv_ship_company, R.id.tv_select_ship_company})
    void selectCompany() {
        ActivityLauncher.startToExpressCompanySelectActivity(this, 1001);
    }

    @OnClick(R.id.btn_sure_to_ship)
    void commit() {
        if (mSelectedExpressCompanyInfo == null) {
            UIHelper.toast("请选择物流公司");
            return;
        }
        String qrCode = isEditMode ? mEdInputShipNum.getNonFormatText() : mTvShipOrder.getText().toString().replace(" ", "");
        if (TextUtils.isEmpty(qrCode)) {
            UIHelper.toast("请输入物流单号");
            return;
        }
        RetrofitClient.get()
                .create(GoodsApis.class)
                .deliverGoods(mOption.orderId, mSelectedExpressCompanyInfo.getCode(), qrCode)
                .compose(this.<ObjectData<String>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData<String>>(getContext(), true, true) {
                    @Override
                    public void onNext(ObjectData<String> result) {
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            ExpressCompanyInfo companyInfo = data.getParcelableExtra("result");
            if (companyInfo != null) {
                onSelectedCompany(companyInfo);
            }
        }
    }

    public static class Option extends BaseActivityOption<Option> {
        boolean isEditMode;
        private String mQRCode;
        private String orderId;

        public Option setOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Option setQRCode(String QRCode) {
            mQRCode = QRCode;
            if (TextUtils.isEmpty(QRCode)) {
                isEditMode = true;
            }
            return this;
        }
    }
}
