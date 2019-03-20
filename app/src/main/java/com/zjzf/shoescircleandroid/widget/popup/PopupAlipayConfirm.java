package com.zjzf.shoescircleandroid.widget.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.utils.MultiSpanUtil;
import com.zjzf.shoescircle.ui.widget.textview.button.ExButton;
import com.zjzf.shoescircleandroid.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 陈志远 on 2019/2/23.
 */
public class PopupAlipayConfirm extends BasePopupWindow {
    ExButton btnCancel;
    ExButton btnConfirm;
    TextView tvName;
    TextView tvAccount;

    View.OnClickListener mOnClickListener;

    public PopupAlipayConfirm(Context context) {
        super(context);
        this.btnCancel = findViewById(R.id.btn_cancel);
        this.btnConfirm = findViewById(R.id.btn_confirm);
        this.tvName = findViewById(R.id.tv_name);
        this.tvAccount = findViewById(R.id.tv_account);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(v);
                }
                dismiss();
            }
        });
        setBlurBackgroundEnable(true);
        setClipChildren(false);

    }

    public void showPopupWindow(String name, String account) {
        MultiSpanUtil.create("真实姓名：" + name)
                .append(name).setTextColor(Color.parseColor("#1A1A1A")).setTextType(Typeface.DEFAULT_BOLD)
                .into(tvName);
        MultiSpanUtil.create("支付宝账号：" + account)
                .append(account).setTextColor(Color.parseColor("#1A1A1A")).setTextType(Typeface.DEFAULT_BOLD)
                .into(tvAccount);
        super.showPopupWindow();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_resure_alipay);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        Animation animation = getDefaultScaleAnimation(true);
        animation.setInterpolator(new OvershootInterpolator(1f));
        return animation;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        Animation animation = getDefaultAlphaAnimation(false);
        animation.setInterpolator(new FastOutSlowInInterpolator());
        return animation;
    }

    public void setConfirmClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

}
