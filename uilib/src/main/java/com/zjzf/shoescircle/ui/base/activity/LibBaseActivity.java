package com.zjzf.shoescircle.ui.base.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.zjzf.shoescircle.lib.base.interfaces.ClearMemoryObject;
import com.zjzf.shoescircle.lib.helper.PermissionHelper;
import com.zjzf.shoescircle.lib.interfaces.IPermission;
import com.zjzf.shoescircle.lib.interfaces.OnPermissionGrantListener;
import com.zjzf.shoescircle.lib.utils.KeyBoardUtil;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.ui.helper.StatusBarHelper;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.ui.widget.EmptyView;
import com.zjzf.shoescircle.ui.widget.StatusBarViewPlaceHolder;
import com.zjzf.shoescircle.ui.widget.TitleBarView;
import com.zjzf.shoescircle.uilib.R;

import java.util.List;

/**
 * Created by 陈志远 on 2018/3/5.
 */
public abstract class LibBaseActivity extends AppCompatActivity implements TitleBarView.OnTitlebarClickCallback, ClearMemoryObject, IPermission {
    protected final String TAG = this.getClass().getSimpleName();

    private PermissionHelper mPermissionHelper;
    protected TitleBarView mTitleBarView;
    protected EmptyView mEmptyView;
    protected StatusBarViewPlaceHolder mStatusBarHolder;
    private StatusBarConfig mStatusBarConfig;
    private volatile boolean isInitStatusConfig = false;

    private volatile boolean hasSetContent = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onSuperCreate(savedInstanceState);
        onHandleIntent(getIntent());
        onPrepareInit(savedInstanceState);
        mStatusBarConfig = new StatusBarConfig();
        onApplyStatusBarConfig(mStatusBarConfig);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
    }

    protected void onSuperCreate(Bundle savedInstanceState) {

    }


    //-----------------------------------------abstract-----------------------------------------

    /**
     * 如果有intent，则需要处理这个intent（该方法在onCreate里面执行，但在setContentView之前调用）
     */
    public abstract void onHandleIntent(Intent intent);

    public abstract int getLayoutId();

    /**
     * oncreate中回调该方法，在setContentView前调用
     */
    protected abstract void onPrepareInit(@Nullable Bundle savedInstanceState);

    /**
     * setContentView后调用，尽量不要再此方法内调用setContentView（已针对处理）
     */
    protected abstract void onInitView(View decorView);

    public String getPageName() {
        return TAG;
    }

    //-----------------------------------------life callback-----------------------------------------


    @Override
    public void finish() {
        KeyBoardUtil.close(this);
        super.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPermissionHelper != null) {
            mPermissionHelper.handleDestroy();
            mPermissionHelper = null;
        }
        mTitleBarView = null;
        mEmptyView = null;
        mStatusBarHolder = null;
    }

    //-----------------------------------------tools method-----------------------------------------
    public LibBaseActivity getContext() {
        return LibBaseActivity.this;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onAfterInitContentView();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onAfterInitContentView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        onAfterInitContentView();
    }

    //-----------------------------------------titlebar-----------------------------------------
    protected void onAfterInitContentView() {
        onInitStatusBar();
        this.mTitleBarView = (TitleBarView) findViewById(R.id.title_bar_view);
        this.mEmptyView = (EmptyView) findViewById(R.id.py_empty_view);
        this.mStatusBarHolder = findViewById(R.id.py_statusbar_placeholder);
        if (mTitleBarView != null) {
            mTitleBarView.setTitlebarClickCallback(this);
        }
        if (!hasSetContent) {
            onSetContentView();
            onInitView(getWindow().getDecorView());
            hasSetContent = true;
        }
    }

    protected void onApplyStatusBarConfig(@NonNull StatusBarConfig config) {

    }

    protected void onInitStatusBar() {
        if (mStatusBarConfig == null || isInitStatusConfig) return;
        onInitStatusBarInternal(mStatusBarConfig);
        isInitStatusConfig = true;
    }

    protected void onInitStatusBarInternal(StatusBarConfig config) {
        if (config == null) return;
        if (config.translucentStatus) {
            StatusBarHelper.setTranslucentStatus(this);
        }
        StatusBarHelper.setRootViewFitsSystemWindows(this, config.fitsSystemWindows);
        StatusBarHelper.setStatusBarFontIconDark(this, config.darkMode);
        if (!config.translucentStatus) {
            StatusBarHelper.setStatusBarColor(this, config.statusBarColor);
        }
    }

    //-----------------------------------------status bar config-----------------------------------------
    public static class StatusBarConfig {
        private boolean fitsSystemWindows = true;
        private boolean translucentStatus = false;
        private boolean darkMode = true;
        private int statusBarColor = Color.WHITE;

        public StatusBarConfig setFitsSystemWindows(boolean fitsSystemWindows) {
            this.fitsSystemWindows = fitsSystemWindows;
            return this;
        }

        public StatusBarConfig setTranslucentStatus(boolean translucentStatus) {
            this.translucentStatus = translucentStatus;
            return this;
        }

        public StatusBarConfig setDarkMode(boolean darkMode) {
            this.darkMode = darkMode;
            return this;
        }

        public StatusBarConfig setStatusBarColor(int statusBarColor) {
            this.statusBarColor = statusBarColor;
            return this;
        }

        public void reset() {
            fitsSystemWindows = true;
            translucentStatus = false;
            darkMode = true;
            statusBarColor = Color.WHITE;
        }
    }

    //-----------------------------------------titlebar-----------------------------------------

    protected void onSetContentView() {
    }

    @Override
    public void onTitleLeftClick() {
        finish();
    }

    @Override
    public void onTitleRightClick() {

    }

    @Override
    public void onTitleClick() {

    }

    @Override
    public void onTitleDoubleClick() {

    }

    @Override
    public boolean onTitleLongClick() {
        return false;
    }


    public void setTitleMode(@TitleBarView.TitleBarMode int mode) {
        if (mTitleBarView != null) {
            mTitleBarView.setMode(mode);
        }
    }

    public void setTitleText(@StringRes int titleText) {
        if (mTitleBarView != null) {
            mTitleBarView.setTitleText(titleText);
        }
    }

    public void setTitleText(String titleText) {
        if (mTitleBarView != null) {
            mTitleBarView.setTitleText(titleText);
        }
    }

    public void setSubTitleText(String subTitleText) {
        if (mTitleBarView != null) {
            mTitleBarView.setSubTitleText(subTitleText);
        }
    }

    public void setTitleTextSize(int spSize) {
        if (mTitleBarView != null) {
            mTitleBarView.setTitleTextSize(spSize);
        }
    }

    public void setTitleTextColor(@ColorInt int color) {
        if (mTitleBarView != null) {
            mTitleBarView.setTitleTextColor(color);
        }
    }

    public void setTitleTransparentMode(boolean isTransparent) {
        if (mTitleBarView != null) {
            mTitleBarView.setTransparentMode(isTransparent);
        }
    }

    public void bindTitleBarAlphaChangeView(View target, View totalHeightView, @Nullable TitleBarView.OnAlphaChangeListener listener) {
        if (mTitleBarView != null) {
            mTitleBarView.bindAlphaChangeView(target, totalHeightView, listener);
        }
    }

    public void bindTitleBarAlphaChangeView(View target, int totalHeight, @Nullable TitleBarView.OnAlphaChangeListener listener) {
        if (mTitleBarView != null) {
            mTitleBarView.bindAlphaChangeView(target, totalHeight, listener);
        }
    }

    @Nullable
    public TitleBarView getTitleBarView() {
        return mTitleBarView;
    }

    @Nullable
    public EmptyView getEmptyView() {
        return mEmptyView;
    }

    protected void handleEmptyView(View view, List<?> datas, View emptyView) {
        boolean isDataEmpty = ToolUtil.isListEmpty(datas);
        ViewUtil.setViewsVisible(isDataEmpty ? View.VISIBLE : View.GONE, emptyView);
        ViewUtil.setViewsVisible(isDataEmpty ? View.GONE : View.VISIBLE, view);
    }
    //-----------------------------------------old request support-----------------------------------------

    @Override
    public void clearMemory(boolean needSetNull) {

    }

    //-----------------------------------------permission-----------------------------------------
    public PermissionHelper getPermissionHelper() {
        if (mPermissionHelper == null) {
            mPermissionHelper = new PermissionHelper(this);
        }
        return mPermissionHelper;
    }

    public void requestPermission(OnPermissionGrantListener listener, PermissionHelper.Permission... permissions) {
        getPermissionHelper().requestPermission(listener, permissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getPermissionHelper().handlePermissionsResult(requestCode, permissions, grantResults);
    }
}