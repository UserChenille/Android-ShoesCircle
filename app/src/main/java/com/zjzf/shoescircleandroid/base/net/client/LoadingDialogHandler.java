package com.zjzf.shoescircleandroid.base.net.client;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.zjzf.shoescircleandroid.widget.LoadingDialog;


public class LoadingDialogHandler extends Handler {

    public static final int SHOW_LOADING_DIALOG = 1;
    public static final int DISMISS_LOADING_DIALOG = 2;

    private Context mContext;
    private boolean cancelable;
    private LoadingDialogCancelListener mLoadingCancelListener;
    private Dialog loadingDialog;

    public LoadingDialogHandler(Context context, Dialog dialog) {
        super();
        this.mContext = context;
        this.loadingDialog = dialog;
    }

    public LoadingDialogHandler(Context context, LoadingDialogCancelListener loadingCancelListener,
                                boolean cancelable) {
        super();
        this.mContext = context;
        this.mLoadingCancelListener = loadingCancelListener;
        this.cancelable = cancelable;
    }

    /**
     * 能否取消对话框
     *
     * @param cancelable
     */
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public void setLoadingCancelListener(LoadingDialogCancelListener loadingCancelListener) {
        mLoadingCancelListener = loadingCancelListener;
    }

    private void initProgressDialog() {

        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.create(mContext);
        }

        loadingDialog.setCancelable(cancelable);

        if (cancelable) {
            loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    if (mLoadingCancelListener != null) {
                        mLoadingCancelListener.onCancelLoading();
                    }
                }
            });
        }

        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }

    }

    private void dismissProgressDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
//            loadingDialog = null;
            removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_LOADING_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_LOADING_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

    /**
     * 正在加载中的对话框回调监听。。。
     */
    public interface LoadingDialogCancelListener {
        void onCancelLoading();
    }
}
