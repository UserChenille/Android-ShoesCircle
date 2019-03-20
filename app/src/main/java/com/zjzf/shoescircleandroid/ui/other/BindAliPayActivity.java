package com.zjzf.shoescircleandroid.ui.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.zjzf.shoescircle.lib.utils.KeyBoardUtil;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.lib.utils.TextWatcherAdapter;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.ui.widget.edit.ExEditText;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.manager.UserInfoManager;
import com.zjzf.shoescircleandroid.base.net.apis.UserApis;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.model.BaseActivityOption;
import com.zjzf.shoescircleandroid.widget.popup.PopupAlipayConfirm;

import butterknife.BindView;

/**
 * Created by 陈志远 on 2019/1/6.
 */
public class BindAliPayActivity extends BaseActivity {

    @BindView(R.id.ed_real_name)
    ExEditText mEdRealName;
    @BindView(R.id.ed_ali_account)
    ExEditText mEdAliAccount;

    private PopupAlipayConfirm mPopupAlipayConfirm;

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_ali_pay;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onInitView(View decorView) {
        getTitleBarView().setRightTextColor(UIHelper.getColor(R.color.btn_disable));
        ViewUtil.setEditTextWatcher(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                checkNextEnable();
            }
        }, mEdRealName, mEdAliAccount);

    }

    private void checkNextEnable() {
        String realName = mEdRealName.getNonFormatText();
        String account = mEdAliAccount.getNonFormatText();

        boolean enable = StringUtil.noEmpty(account, realName);
        getTitleBarView().setRightTextColor(enable ? UIHelper.getColor(R.color.common_red) : UIHelper.getColor(R.color.btn_disable));
    }

    @Override
    public void onTitleRightClick() {
        KeyBoardUtil.close(this);
        final String realName = mEdRealName.getNonFormatText();
        final String account = mEdAliAccount.getNonFormatText();

        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(realName)) {
            UIHelper.toast("姓名不能为空");
            return;
        }

        if (!TextUtils.equals(account, account)) {
            UIHelper.toast("帐号不能为空");
            return;
        }
        if (mPopupAlipayConfirm == null) {
            mPopupAlipayConfirm = new PopupAlipayConfirm(getContext());
        }
        mPopupAlipayConfirm.setConfirmClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm(realName, account);
            }
        });
        mPopupAlipayConfirm.showPopupWindow(realName, account);
    }

    private void confirm(String realName, String account) {
        RetrofitClient.get()
                .create(UserApis.class)
                .bindAliPay(realName, account)
                .compose(this.<ObjectData>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData>(getContext(), true, true) {
                    @Override
                    public void onNext(ObjectData result) {
                        UserInfoManager.INSTANCE.updateUserInfo();
                        UIHelper.toast("绑定成功");
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    public static class Option extends BaseActivityOption<Option> {

    }
}
