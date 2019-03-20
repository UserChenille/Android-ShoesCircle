package com.zjzf.shoescircleandroid.ui.transaction.scan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.helper.PermissionHelper;
import com.zjzf.shoescircle.lib.interfaces.OnPermissionGrantListener;
import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;
import com.zjzf.shoescircleandroid.model.BaseActivityOption;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * Created by 陈志远 on 2018/12/9.
 */
public class QRScanActivity extends BaseActivity implements QRCodeView.Delegate {
    @BindView(R.id.qr_scan)
    ZXingView mQrScan;
    @BindView(R.id.layout_input_qr)
    LinearLayout mLayoutInputQr;
    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.tv_flash)
    TextView mTvFlash;

    boolean isOpenedFlash = false;

    private Option mOption;

    private AtomicBoolean isSucceed = new AtomicBoolean(false);

    @Override
    public void onHandleIntent(Intent intent) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_qr_scan;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {
        mOption = getActivityOption(Option.class);
        if (mOption == null || TextUtils.isEmpty(mOption.orderId)) {
            finish();
            return;
        }
        requestPermission(new OnPermissionGrantListener.OnPermissionGrantListenerAdapter() {
            @Override
            public void onPermissionsDenied(PermissionHelper.Permission... deniedPermissions) {
                super.onPermissionsDenied(deniedPermissions);
                UIHelper.toast("扫描二维码需要打开相机和闪光灯权限");
            }
        }, PermissionHelper.Permission.CAMERA, PermissionHelper.Permission.READ_EXTERNAL_STORAGE, PermissionHelper.Permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void onApplyStatusBarConfig(@NonNull StatusBarConfig config) {
        super.onApplyStatusBarConfig(config);
        config.setStatusBarColor(Color.BLACK);
        config.setDarkMode(false);
    }

    @Override
    protected void onInitView(View decorView) {
        if (mOption == null || TextUtils.isEmpty(mOption.orderId)) {
            finish();
            return;
        }
        mQrScan.setDelegate(this);
        mQrScan.getScanBoxView().setTipText("对准物流单上的条形码");
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSucceed.compareAndSet(true, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQrScan.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mQrScan.startSpotAndShowRect(); // 显示扫描框，并且延迟0.1秒后开始识别
    }

    @Override
    protected void onStop() {
        mQrScan.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQrScan.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (isSucceed.get()) {
            return;
        }
        isSucceed.compareAndSet(false, true);
        mQrScan.startSpotAndShowRect();
        ActivityLauncher.startToQRCodeScanResultOrEditActivity(getContext(), new QRScanResultOrEditActivity.Option()
                .setQRCode(result)
                .setOrderId(mOption.orderId));
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        String tipText = mQrScan.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mQrScan.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
                mTvFlash.setVisibility(View.VISIBLE);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mQrScan.getScanBoxView().setTipText(tipText);
            }
            if (!isOpenedFlash) {
                mTvFlash.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.tv_flash)
    void toggleFlash() {
        if (isOpenedFlash) {
            mQrScan.closeFlashlight();
            isOpenedFlash = false;
            ViewUtil.setTextViewDrawable(mTvFlash, 0, R.drawable.ic_flash, 0, 0);
            mTvFlash.setText("打开闪光灯");
        } else {
            mQrScan.openFlashlight();
            isOpenedFlash = true;
            ViewUtil.setTextViewDrawable(mTvFlash, 0, R.drawable.ic_flash_close, 0, 0);
            mTvFlash.setText("关闭闪光灯");
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        LogHelper.trace(LogHelper.e, TAG, "打开相机出错");
    }

    @OnClick(R.id.layout_input_qr)
    void toInputQR() {
        ActivityLauncher.startToQRCodeScanResultOrEditActivity(this, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @OnClick(R.id.iv_close)
    @Override
    public void finish() {
        super.finish();
    }

    public static class Option extends BaseActivityOption<Option> {
        private String orderId;

        public Option setOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }
    }
}
