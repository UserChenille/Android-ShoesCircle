package com.zjzf.shoescircleandroid.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.zjzf.shoescircle.lib.helper.RxHelper;
import com.zjzf.shoescircle.lib.interfaces.SimpleCallback;
import com.zjzf.shoescircle.lib.net.retrofit.ActivityEvent;
import com.zjzf.shoescircle.lib.net.retrofit.LifecycleTransformer;
import com.zjzf.shoescircle.lib.net.retrofit.RxLifecycle;
import com.zjzf.shoescircle.lib.utils.EventBusUtil;
import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.ui.base.activity.LibBaseActivity;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.interfaces.QQLoginListenerAdapter;
import com.zjzf.shoescircleandroid.base.net.client.HandleServerApiErrorAction;
import com.zjzf.shoescircleandroid.model.BaseActivityOption;
import com.zjzf.shoescircleandroid.model.UserInfo;
import com.zjzf.shoescircleandroid.model.event.CloseEvent;
import com.zjzf.shoescircleandroid.model.event.LoginSuccessEvent;
import com.zjzf.shoescircleandroid.model.event.LogoutEvent;
import com.zjzf.shoescircleandroid.widget.LoadingDialog;

import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.BehaviorSubject;
import razerdp.util.InputMethodUtils;

/**
 * Created by 陈志远 on 2018/7/22.
 */
public abstract class BaseActivity extends LibBaseActivity {
    public static final String INTENT_ACTIVITY_OPTION = "activityOption";

    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    private Unbinder mUnbinder;
    private BaseActivityOption option;
    private LoadingDialog mLoadingDialog;
    protected boolean onBackground;

    /**
     * 按需注册事件
     *
     * @param event
     */
    @Subscribe
    public void onEvent(CloseEvent event) {
        if (event.contains(this.getClass())) {
            LogHelper.trace("收到关闭Activity的event  >>>  关闭【" + this.getClass().getSimpleName() + "】");
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtil.register(this);
        LogHelper.trace("打开Activity " + ToolUtil.wrapLocation(this.getClass(), 1));
    }

    @Subscribe
    public final void onEvent(LoginSuccessEvent event) {
        onLoginSuccess(event.getUserInfo());
    }

    @Subscribe
    public final void onEvent(LogoutEvent event) {
        onLogOut();
    }

    public void onLoginSuccess(UserInfo userInfo) {

    }

    public void onLogOut() {

    }

    protected <T extends BaseActivityOption> T getActivityOption(Class<T> optionClass) {
        if (option == null) {
            return null;
        }
        if (option.getClass() == optionClass) {
            return (T) option;
        }

        return null;
    }

    @Override
    protected void onSetContentView() {
        super.onSetContentView();
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Tencent.onActivityResultData(requestCode, resultCode, data, new QQLoginListenerAdapter.EmptyQQloginListener());

        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, new QQLoginListenerAdapter.EmptyQQloginListener());
            }

        }
    }

    /**
     * 绑定retrofit 网络请求的生命周期
     */
    protected final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    public <T> ObservableTransformer<T, T> normalSchedulerTransformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .doOnNext(new HandleServerApiErrorAction<>())
                        .compose(BaseActivity.this.<T>bindUntilEvent(ActivityEvent.DESTROY))
                        .compose(RxHelper.<T>io_main());
            }
        };
    }

    @Override
    protected void onSuperCreate(Bundle savedInstanceState) {
        super.onSuperCreate(savedInstanceState);
        option = (BaseActivityOption) getIntent().getSerializableExtra(INTENT_ACTIVITY_OPTION);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        onBackground = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifecycleSubject.onNext(ActivityEvent.STOP);
        onBackground = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
        onBackground = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onBackground = false;
    }

    @Override
    public void finish() {
        InputMethodUtils.close(this);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }


    protected void showLoadingDialog() {
        showLoadingDialog(true, null);
    }

    protected void showLoadingDialog(boolean cancelable, final SimpleCallback<Void> dismissCallback) {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.create(getContext());
        }

        mLoadingDialog.setCanCancel(cancelable);
        if (cancelable) {
            mLoadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (dismissCallback != null) {
                        dismissCallback.onCall(null);
                    }
                }
            });
        } else {
            mLoadingDialog.setOnDismissListener(null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!mLoadingDialog.isShowing() && !isDestroyed()) {
                mLoadingDialog.show();
            }
        } else {
            if (!mLoadingDialog.isShowing() && !isFinishing()) {
                mLoadingDialog.show();
            }
        }
    }

    protected void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public boolean isOnBackground() {
        return onBackground;
    }

}
