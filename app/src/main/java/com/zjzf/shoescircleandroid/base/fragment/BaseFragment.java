package com.zjzf.shoescircleandroid.base.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.zjzf.shoescircle.lib.helper.PermissionHelper;
import com.zjzf.shoescircle.lib.helper.RxHelper;
import com.zjzf.shoescircle.lib.helper.rx.base.RxCall;
import com.zjzf.shoescircle.lib.interfaces.IPermission;
import com.zjzf.shoescircle.lib.interfaces.OnPermissionGrantListener;
import com.zjzf.shoescircle.lib.net.retrofit.FragmentEvent;
import com.zjzf.shoescircle.lib.net.retrofit.LifecycleTransformer;
import com.zjzf.shoescircle.lib.net.retrofit.RxLifecycle;
import com.zjzf.shoescircle.lib.utils.EventBusUtil;
import com.zjzf.shoescircle.lib.utils.KeyBoardUtil;
import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircle.lib.utils.TimeUtil;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.ui.widget.EmptyView;
import com.zjzf.shoescircle.uilib.R;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.interfaces.QQLoginListenerAdapter;
import com.zjzf.shoescircleandroid.base.net.client.HandleServerApiErrorAction;
import com.zjzf.shoescircleandroid.model.UserInfo;
import com.zjzf.shoescircleandroid.model.event.CloseEvent;
import com.zjzf.shoescircleandroid.model.event.LoginSuccessEvent;
import com.zjzf.shoescircleandroid.model.event.LogoutEvent;
import com.zjzf.shoescircleandroid.widget.LoadingDialog;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/**
 * Created by 陈志远 on 2018/2/27.
 */
public abstract class BaseFragment extends Fragment implements IPermission {
    protected final String TAG = this.getClass().getSimpleName();

    protected View rootView;
    private PermissionHelper mPermissionHelper;
    protected EmptyView mEmptyView;
    protected LoadingDialog dialog;
    public static final int DELAY_LOAD_DATA = 300;
    protected boolean onBackground;
    protected AtomicBoolean showLock = new AtomicBoolean(false);
    protected AtomicBoolean hideLock = new AtomicBoolean(false);
    private long lastShowingTime;


    private final io.reactivex.subjects.BehaviorSubject<FragmentEvent> lifecycleSubject = io.reactivex.subjects.BehaviorSubject.create();

    /**
     * @param event
     */
    @Subscribe
    public void onEvent(CloseEvent event) {
        if (event.contains(this.getClass())) {
            LogHelper.trace("收到关闭Fragment的event  >>>  关闭【" + this.getClass().getSimpleName() + "】");
            back();
        }
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

    /**
     * 绑定retrofit 网络请求的生命周期
     */
    protected final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    /**
     * 常规的网络请求变换
     *
     * @param <T>
     * @return
     */
    public final <T> ObservableTransformer<T, T> normalSchedulerTransformer() {
        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.doOnNext(new HandleServerApiErrorAction<>())
                        .compose(RxHelper.<T>io_main())
                        .compose(BaseFragment.this.<T>bindUntilEvent(FragmentEvent.DESTROY_VIEW));
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible()) {
            handleShow();
        }
        lifecycleSubject.onNext(FragmentEvent.RESUME);
        onBackground = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        onBackground = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
    }

    @Override
    public void onStop() {
        super.onStop();
        handleHide();
        lifecycleSubject.onNext(FragmentEvent.STOP);
        onBackground = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lifecycleSubject.onNext(FragmentEvent.DETACH);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            onGetArguments(getArguments());
        } else {
            onNoArguments();
        }
        if (mPermissionHelper == null) {
            mPermissionHelper = new PermissionHelper(this);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBusUtil.register(this);
        LogHelper.trace("当前打开fragment： " + ToolUtil.wrapLocation(this.getClass(), 1));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, rootView);
            this.mEmptyView = (EmptyView) rootView.findViewById(R.id.py_empty_view);
            initViews();
            RxHelper.delay(DELAY_LOAD_DATA, new RxCall<Long>() {
                @Override
                public void onCall(Long data) {
                    loadNetData();
                }
            });
        }
        beforeReturenRootView();
        return UIHelper.cleanView(rootView);
    }

    @Override
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        if (mPermissionHelper != null) {
            mPermissionHelper.handleDestroy();
        }
        mPermissionHelper = null;
    }

    protected void showLoading() {
        showLoading(null);
    }

    protected void showLoading(CharSequence message) {
        if (dialog == null) {
            dialog = LoadingDialog.create(getContext());
        }
        dialog.show(message);
    }

    protected void dismissLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    //=============================================================show/hide
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisibleToUser = rootView != null && isVisibleToUser;
        if (isVisibleToUser) {
            handleShow();
        } else {
            handleHide();
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (rootView != null) {
            if (hidden) {
                handleHide();
            } else {
                handleShow();
            }
        }
    }


    public final void handleShow() {
        if (showLock.get()) return;
        showLock.compareAndSet(false, true);
        onShow();
        hideLock.compareAndSet(true, false);
    }
    public final void handleHide() {
        if (hideLock.get()) return;
        hideLock.compareAndSet(false, true);
        onHide();
        lastShowingTime = System.currentTimeMillis();
        showLock.compareAndSet(true, false);
    }

    public void onShow() {

    }

    public void onHide() {

    }
    //-----------------------------------------abstract-----------------------------------------

    /**
     * 获得bundle数据
     *
     * @param arguments 不会为空
     */
    protected void onGetArguments(@NonNull Bundle arguments) {

    }

    /**
     * 没有数据的情况
     */
    protected void onNoArguments() {
    }

    protected abstract int getLayoutId();

    protected abstract void initViews();

    /**
     * 在{@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}返回view之前，无论rootview在不在，都会被回调
     */
    protected void beforeReturenRootView() {

    }

    /**
     * 加载网络接口
     */
    protected abstract void loadNetData();

    protected void handleEmptyView(View view, List<?> datas, View emptyView) {
        boolean isDataEmpty = ToolUtil.isListEmpty(datas);
        ViewUtil.setViewsVisible(isDataEmpty ? View.VISIBLE : View.GONE, emptyView);
        ViewUtil.setViewsVisible(isDataEmpty ? View.GONE : View.VISIBLE, view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Tencent.onActivityResultData(requestCode, resultCode, data, new QQLoginListenerAdapter.EmptyQQloginListener());

        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, new QQLoginListenerAdapter.EmptyQQloginListener());
            }

        }
    }

    protected void back() {
        KeyBoardUtil.close(getActivity());
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            getActivity().finish();
        }
    }
    /**
     * 是否超出了刷新时间
     */
    public boolean isOverRefreshTime() {
        return isOverRefreshTime(TimeUtil.SECOND * 10 * 1000);
    }

    public boolean isOverRefreshTime(long area) {
        long curTime = System.currentTimeMillis();
        return curTime - lastShowingTime > area;
    }
}
