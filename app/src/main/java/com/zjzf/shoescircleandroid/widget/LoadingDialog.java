package com.zjzf.shoescircleandroid.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.ui.widget.LoadingView;
import com.zjzf.shoescircle.ui.widget.dialog.BaseDialog;
import com.zjzf.shoescircleandroid.R;


/**
 * Created by 陈志远 on 2017/5/3.
 * <p>
 * 加载中dialog
 */

public class LoadingDialog extends BaseDialog {

    private LoadingView mLoading;
    private TextView mDescTextView;
    private CharSequence desc;

    protected LoadingDialog(@NonNull Context context) {
        this(context, R.style.loading_dialog);
        setCancelable(false);
    }

    protected LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public static LoadingDialog create(@NonNull Context context) {
        return new LoadingDialog(context);
    }

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_loading, null);
    }

    @Override
    protected void onFindView(@NonNull View dialogView) {
        if (mLoading == null) {
            mLoading = (LoadingView) dialogView.findViewById(R.id.loading_view);
        }
        mLoading.start();
        if (mDescTextView == null) mDescTextView = dialogView.findViewById(R.id.tv_desc);
        ViewUtil.setViewsVisible(StringUtil.noEmpty(desc) ? View.VISIBLE : View.GONE, mDescTextView);
        mDescTextView.setText(desc);
    }

    @Override
    protected void onInitMode(@DialogMode int mode) {

    }

    @Override
    protected int onInitWindowWidth() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    public void show(CharSequence desc) {
        this.desc = desc;
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mLoading != null) {
            mLoading.stop();
        }
    }

    @Override
    public void show() {
        show(null);
    }
}
