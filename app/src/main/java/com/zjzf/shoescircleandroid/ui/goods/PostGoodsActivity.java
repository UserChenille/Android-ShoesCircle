package com.zjzf.shoescircleandroid.ui.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.helper.RxHelper;
import com.zjzf.shoescircle.lib.helper.rx.base.RxCall;
import com.zjzf.shoescircle.lib.net.client.NetClient;
import com.zjzf.shoescircle.lib.utils.EventBusUtil;
import com.zjzf.shoescircle.lib.utils.TextWatcherAdapter;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.widget.edit.ExEditText;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.net.apis.GoodsApis;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ListData;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.model.AutoInputInfo;
import com.zjzf.shoescircleandroid.model.event.RefreshEvent;
import com.zjzf.shoescircleandroid.ui.main.IndexFragment;
import com.zjzf.shoescircleandroid.widget.popup.PopupAutoInput;
import com.zjzf.shoescircleandroid.widget.popup.PopupSelectGoodsPhoto;
import com.zjzf.shoescircleandroid.widget.popup.PopupSelectSize;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 陈志远 on 2018/8/29.
 */
public class PostGoodsActivity extends BaseActivity {

    @BindView(R.id.ed_name)
    ExEditText mEdName;
    @BindView(R.id.iv_arrow_1)
    ImageView mIvArrow1;
    @BindView(R.id.ll_name)
    LinearLayout mLlName;
    @BindView(R.id.ed_art_no)
    ExEditText mEdArtNo;
    @BindView(R.id.iv_arrow_2)
    ImageView mIvArrow2;
    @BindView(R.id.ll_art_no)
    LinearLayout mLlArtNo;
    @BindView(R.id.tv_size)
    TextView mTvSize;
    @BindView(R.id.ll_size)
    LinearLayout mLlSize;
    @BindView(R.id.ed_prise)
    ExEditText mEdPrise;
    @BindView(R.id.ll_prise)
    LinearLayout mLlPrise;
    private PopupSelectGoodsPhoto mSelectGoodsPhoto;
    private String photoUrl;
    private String sizeStr;
    private boolean prevAutoSearch;

    private PopupAutoInput mPopupAutoInput;
    private View lastAnimaArrow;
    private PopupSelectSize mPopupSelectSize;

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_post_goods;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onInitView(View decorView) {
        getTitleBarView().setRightTextColor(UIHelper.getColor(R.color.btn_disable));

        mSelectGoodsPhoto = new PopupSelectGoodsPhoto(getContext());
        mSelectGoodsPhoto.setOnPhotoSelectListener(new PopupSelectGoodsPhoto.OnPhotoSelectListener() {
            @Override
            public void onSelect(String url) {
                photoUrl = url;
                confirm();
            }
        });

        RxHelper.debounceListenEdittext(mEdName, 300, new RxCall<String>() {
            @Override
            public void onCall(String data) {
                checkRightClickEnable(false);
                queryName(data);
            }
        });

        RxHelper.debounceListenEdittext(mEdArtNo, 300, new RxCall<String>() {
            @Override
            public void onCall(String data) {
                checkRightClickEnable(false);
                queryArtNo(data);
            }
        });

        mTvSize.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                checkRightClickEnable(false);
            }
        });
    }

    private void queryName(final String key) {
        if (TextUtils.isEmpty(key)) return;
        if (prevAutoSearch) {
            prevAutoSearch = false;
            return;
        }
        RetrofitClient.get()
                .create(GoodsApis.class)
                .queryAutoInput(1, 5, "", key)
                .compose(this.<ListData<AutoInputInfo>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ListData<AutoInputInfo>>(false) {
                    @Override
                    public void onNext(ListData<AutoInputInfo> result) {
                        List<AutoInputInfo> dataList = result.getData().getList();
                        if (!ToolUtil.isListEmpty(dataList)) {
                            for (AutoInputInfo datum : dataList) {
                                datum.setType(0);
                            }
                            showAutoFillPopup(dataList, key, 0, mIvArrow1);
                        }
                    }
                });
    }

    private void rotateArrow(View v) {
        lastAnimaArrow = v;
        RotateAnimation rotateAnimation = new RotateAnimation(0, 90f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(300);
        rotateAnimation.setFillAfter(true);
        v.startAnimation(rotateAnimation);
    }

    private void restoreArrow() {
        if (lastAnimaArrow != null) {
            RotateAnimation rotateAnimation = new RotateAnimation(90f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(300);
            rotateAnimation.setFillAfter(true);
            lastAnimaArrow.startAnimation(rotateAnimation);
        }
    }

    private void queryArtNo(final String key) {
        if (TextUtils.isEmpty(key)) return;
        if (prevAutoSearch) {
            prevAutoSearch = false;
            return;
        }
        RetrofitClient.get()
                .create(GoodsApis.class)
                .queryAutoInput(1, 5, key, "")
                .compose(this.<ListData<AutoInputInfo>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ListData<AutoInputInfo>>(false) {
                    @Override
                    public void onNext(ListData<AutoInputInfo> result) {
                        List<AutoInputInfo> dataList = result.getData().getList();
                        if (!ToolUtil.isListEmpty(dataList)) {
                            for (AutoInputInfo datum : dataList) {
                                datum.setType(1);
                            }
                            showAutoFillPopup(dataList, key, 1, mIvArrow2);
                        }
                    }
                });
    }

    private void showAutoFillPopup(List<AutoInputInfo> data, String key, int type, View anchor) {
        if (ToolUtil.isListEmpty(data) || anchor == null) return;
        if (mPopupAutoInput == null) {
            mPopupAutoInput = new PopupAutoInput(getContext());
            mPopupAutoInput.setOnItemClickListener(new PopupAutoInput.OnItemClickListener<AutoInputInfo>() {
                @Override
                public void onItemClicked(int type, AutoInputInfo data) {
                    switch (type) {
                        case 0:
                            mEdName.setText(data.getName());
                            mEdArtNo.setText(data.getFreightNo());
                            mEdName.setSelection(mEdName.getText().length());
                            break;
                        case 1:
                            mEdArtNo.setText(data.getFreightNo());
                            mEdName.setText(data.getName());
                            mEdArtNo.setSelection(mEdArtNo.getText().length());
                            break;
                    }
                    prevAutoSearch = true;
                    photoUrl = data.getPhoto();
                }
            });
            mPopupAutoInput.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    restoreArrow();
                }
            });
        }
        if (mPopupAutoInput.isShowing()) {
            mPopupAutoInput.updateData(anchor, type, data, key);
        } else {
            mPopupAutoInput.showPopupWindow(anchor, type, data, key);
        }
        rotateArrow(anchor);
        switch (type) {
            case 0:
                mEdName.requestFocus();
                break;
            case 1:
                mEdArtNo.requestFocus();
                break;
        }
    }

    private boolean checkRightClickEnable(boolean toast) {
        if (TextUtils.isEmpty(mEdName.getNonFormatText())) {
            if (toast) {
                UIHelper.toast("请填写名称哦");
            }
            getTitleBarView().setRightTextColor(UIHelper.getColor(R.color.btn_disable));
            return false;
        }
        if (TextUtils.isEmpty(mEdArtNo.getNonFormatText())) {
            if (toast) {
                UIHelper.toast("请填写货号哦");
            }
            getTitleBarView().setRightTextColor(UIHelper.getColor(R.color.btn_disable));
            return false;
        }
        if (TextUtils.isEmpty(mTvSize.getText())) {
            if (toast) {
                UIHelper.toast("请选择码数哦");
            }
            getTitleBarView().setRightTextColor(UIHelper.getColor(R.color.btn_disable));
            return false;
        }
        getTitleBarView().setRightTextColor(UIHelper.getColor(R.color.common_red));
        return true;
    }

    @OnClick(R.id.ll_size)
    void selectSize() {
        if (mPopupSelectSize == null) {
            mPopupSelectSize = new PopupSelectSize(getContext());
            mPopupSelectSize.setOnConfirmListener(new PopupSelectSize.OnConfirmListener() {
                @Override
                public void onConfirmListener(List<String> selectedSize) {
                    if (ToolUtil.isListEmpty(selectedSize)) {
                        sizeStr = null;
                    } else {
                        sizeStr = TextUtils.join(",", selectedSize);
                    }
                    mTvSize.setText(sizeStr);
                }
            });
        }
        mPopupSelectSize.showPopupWindow();

    }

    @Override
    public void onTitleRightClick() {
        if (!checkRightClickEnable(true)) return;
        if (TextUtils.isEmpty(photoUrl)) {
            mSelectGoodsPhoto.showPopupWindow(mEdArtNo.getNonFormatText());
        } else {
            confirm();
        }
    }

    private void confirm() {
        String name = mEdName.getNonFormatText();
        String freightNo = mEdArtNo.getNonFormatText();
        String size = mTvSize.getText().toString().trim();
        String price = mEdPrise.getNonFormatText();

        RetrofitClient.get()
                .create(GoodsApis.class)
                .postGoods(NetClient.requestBody()
                        .put("access-token", NetClient.sUserToken)
                        .put("name", name)
                        .put("photo", photoUrl)
                        .put("freightNo", freightNo)
                        .put("size", size)
                        .put("price", price)
                        .build())
                .compose(this.<ObjectData>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData>(getContext()) {
                    @Override
                    public void onNext(ObjectData result) {
                        UIHelper.toast("发布成功");
                        EventBusUtil.post(new RefreshEvent(IndexFragment.class));
                        finish();
                    }
                });


    }
}
