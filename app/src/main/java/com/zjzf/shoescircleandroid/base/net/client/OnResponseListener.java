package com.zjzf.shoescircleandroid.base.net.client;

import android.content.Context;

import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircleandroid.ShoesCircleApp;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class OnResponseListener<T> implements Observer<T>, LoadingDialogHandler.LoadingDialogCancelListener {

    private LoadingDialogHandler mLoadingDialogHandler;
    private Context mContext;
    private boolean mIsShowToast = true;
    private boolean mIsShowLoadingDialog = true;
    private boolean mCanCancelDialog;

    public OnResponseListener(LoadingDialogHandler loadingDialogHandler) {
        mLoadingDialogHandler = loadingDialogHandler;
        mLoadingDialogHandler.setLoadingCancelListener(this);
    }

    public OnResponseListener() {
    }

    public OnResponseListener(Context context, boolean isShowToast, boolean isShowLoadingDialog) {
        mContext = context;
        mIsShowToast = isShowToast;
        mIsShowLoadingDialog = isShowLoadingDialog;
        if (isShowLoadingDialog) {
            initLoadingDialog(context);
        }
    }

    public OnResponseListener(boolean isShowToast) {
        mIsShowToast = isShowToast;
    }

    public OnResponseListener(Context context) {
        mContext = context;
        initLoadingDialog(context);
    }

    private void initLoadingDialog(Context context) {
        mLoadingDialogHandler = new LoadingDialogHandler(context, this, mCanCancelDialog);
    }

    private void showLoadingDialog() {
        if (mIsShowLoadingDialog && mLoadingDialogHandler != null) {
            mLoadingDialogHandler.obtainMessage(LoadingDialogHandler.SHOW_LOADING_DIALOG).sendToTarget();
        }
    }

    private void dismissLoadingDialog() {
        if (mIsShowLoadingDialog && mLoadingDialogHandler != null) {
            mLoadingDialogHandler.obtainMessage(LoadingDialogHandler.DISMISS_LOADING_DIALOG).sendToTarget();
            mLoadingDialogHandler = null;
        }
    }

    public OnResponseListener<T> setContext(Context context) {
        mContext = context;
        return this;
    }

    public OnResponseListener<T> showToast(boolean showToast) {
        mIsShowToast = showToast;
        return this;
    }

    public OnResponseListener<T> showLoadingDialog(boolean showLoadingDialog) {
        mIsShowLoadingDialog = showLoadingDialog;
        return this;
    }

    public OnResponseListener<T> setCanCancelDialog(boolean canCancelDialog) {
        mCanCancelDialog = canCancelDialog;
        return this;
    }

    /**
     * 对错误进行统一处理
     * 隐藏LoadingDialog
     *
     * @param e
     */
    @Deprecated
    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            switch (apiException.getErrorCode()) {
                case ApiException.SESSION_TIMEOUT:
                    // session过期，打开登录界面
                    ActivityLauncher.startToLoginActivity(ShoesCircleApp.getInstance().getTopActivity());
                    break;
                default:
                    break;
            }
            onFailure(apiException, apiException.getErrorCode(), apiException.getResultData() == null ? "" : apiException.getResultData().getMessage());
        }
        e.printStackTrace();
        if (mIsShowToast) {
            UIHelper.toast(e.getLocalizedMessage());
        }
        dismissLoadingDialog();
        onComplete();
    }

    public void onFailure(ApiException e, int code, String message) {
    }

    @Override
    public void onComplete() {
        dismissLoadingDialog();
    }

    private Disposable mDisposable;


    /**
     * 订阅开始时调用
     * 显示LoadingDialog
     */
    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
        showLoadingDialog();
    }

    @Override
    abstract public void onNext(T result);


    /**
     * 取消LoadingDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelLoading() {
        if (mDisposable != null && mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
