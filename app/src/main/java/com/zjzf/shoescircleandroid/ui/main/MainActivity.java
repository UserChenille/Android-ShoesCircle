package com.zjzf.shoescircleandroid.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.helper.rx.ObserverAdapter;
import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.im.IMReceiveObserver;
import com.zjzf.shoescircleandroid.base.manager.RongYunManager;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;
import com.zjzf.shoescircleandroid.helper.UpdateHelper;
import com.zjzf.shoescircleandroid.model.event.RemoveUnreadMessageCountEvent;

import org.greenrobot.eventbus.Subscribe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

public class MainActivity extends BaseActivity implements IMReceiveObserver {

    @Override
    public void onReceived(Message message, int i) {
        addUnReadMessageCount();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TAB_INDEX, TAB_MSG, TAB_ME})
    public @interface Tab {
    }

    public static final int TAB_INDEX = 0x10;
    public static final int TAB_MSG = 0x11;
    public static final int TAB_ME = 0x12;

    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.tv_index)
    TextView mTvIndex;
    @BindView(R.id.rl_index)
    RelativeLayout mRlIndex;
    @BindView(R.id.tv_msg)
    TextView mTvMsg;
    @BindView(R.id.tv_msg_num)
    TextView mTvMsgNum;
    @BindView(R.id.rl_msg)
    RelativeLayout mRlMsg;
    @BindView(R.id.tv_me)
    TextView mTvMe;
    @BindView(R.id.rl_me)
    RelativeLayout mRlMe;

    private int lastSelectedTab = -1;


    Fragment indexFragment;
    Fragment msgFragment;
    Fragment meFragment;

    private int unReadMessageCount;

    /**
     * 按需注册事件
     *
     * @param event
     */
    @Subscribe
    public void onEvent(RemoveUnreadMessageCountEvent event) {
        if (event.getCount() > 0 && event.getCount() <= unReadMessageCount) {
            updateUnReadMessageCount(unReadMessageCount - event.getCount());
        }
    }

    public void addUnReadMessageCount() {
        unReadMessageCount++;
        updateUnReadMessageCount();
    }

    public void updateUnReadMessageCount() {
        updateUnReadMessageCount(unReadMessageCount);
    }

    public void updateUnReadMessageCount(int unReadMessageCount) {
        if (unReadMessageCount > -1) {
            this.unReadMessageCount = unReadMessageCount;
        }
        if (this.unReadMessageCount <= 0) {
            mTvMsgNum.setVisibility(View.INVISIBLE);
            return;
        }
        mTvMsgNum.setVisibility(View.VISIBLE);
        if (mTvMsgNum != null) {
            if (this.unReadMessageCount > 99) {
                mTvMsgNum.setText(String.format(Locale.getDefault(), "%d+", this.unReadMessageCount));
            }
            mTvMsgNum.setText(String.valueOf(unReadMessageCount));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onHandleIntent(intent);
    }

    @Override
    public void onHandleIntent(Intent intent) {
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                String uri = data.toString();
                String scheme = data.getScheme();
                String host = data.getHost();
                String path = data.getPath();

                LogHelper.trace(TAG,
                        "uri > " + uri + "\n" +
                                "scheme > " + scheme + "\n" +
                                "host > " + host + "\n" +
                                "path > " + path);

                if (TextUtils.equals(scheme, "rong") &&
                        TextUtils.equals(host, getPackageName()) &&
                        TextUtils.equals(path, "/conversationlist")) {
                    changeToMsgTab();
                }
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onInitView(View decorView) {
        RongYunManager.INSTANCE.registerReceivedObserver(this);
        changeToIndexTab();
        RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.values(), new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                unReadMessageCount = integer == null ? 0 : integer;
                updateUnReadMessageCount();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
        new UpdateHelper(this).checkUpdate();
    }

    @OnClick(R.id.rl_index)
    void changeToIndexTab() {
        changeTab(TAB_INDEX);
    }

    @OnClick(R.id.rl_msg)
    void changeToMsgTab() {
        changeTab(TAB_MSG);
    }

    @OnClick(R.id.rl_me)
    void changeToMeTab() {
        changeTab(TAB_ME);
    }

    public void changeTab(@Tab int tab) {
        if (this.lastSelectedTab == tab) return;
        hideAllFragments(indexFragment, msgFragment, meFragment);
        mTvIndex.setSelected(false);
        mTvMsg.setSelected(false);
        mTvMe.setSelected(false);
        Fragment showFragment = null;

        switch (tab) {
            case TAB_INDEX:
                if (indexFragment == null) {
                    indexFragment = new IndexFragment();
                }
                showFragment = indexFragment;
                mTvIndex.setSelected(true);
                break;
            case TAB_MSG:
                if (msgFragment == null) {
                    msgFragment = new MsgFragment();
                }
                showFragment = msgFragment;
                mTvMsg.setSelected(true);
                break;
            case TAB_ME:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                }
                showFragment = meFragment;
                mTvMe.setSelected(true);
                break;
        }
        this.lastSelectedTab = tab;
        if (showFragment != null) {
            ActivityLauncher.addFragmentToActivity(getSupportFragmentManager(), showFragment, R.id.container);
        }
    }

    private void hideAllFragments(Fragment... fragments) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : fragments) {
            if (fragment != null && !fragment.isHidden()) {
                ft.hide(fragment);
            }
        }
        ft.commitAllowingStateLoss();
    }

    private boolean mIsExit;
    private static final int EXIT_TIME = 2000;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                finish();
            } else {
                UIHelper.toast("再点一次退出鞋圈");
                mIsExit = true;
                Observable.timer(EXIT_TIME, TimeUnit.MILLISECONDS)
                        .subscribe(new ObserverAdapter<Long>() {
                            @Override
                            public void onNext(Long aLong) {
                                mIsExit = false;
                            }
                        });
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RongYunManager.INSTANCE.unRegisterReceivedObserver(this);
    }
}
