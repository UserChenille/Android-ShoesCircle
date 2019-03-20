package com.zjzf.shoescircleandroid.ui.im;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.im.IMReceiveObserver;
import com.zjzf.shoescircleandroid.base.manager.RongYunManager;
import com.zjzf.shoescircleandroid.model.BaseActivityOption;

import io.rong.imkit.fragment.SubConversationListFragment;
import io.rong.imlib.model.Message;

/**
 * Created by 陈志远 on 2018/11/25.
 */
public class IMSubDetailActivity extends BaseActivity implements IMReceiveObserver {
    SubConversationListFragment mSubConversationListFragment;

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_im_detail;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onInitView(View decorView) {
        RongYunManager.INSTANCE.registerReceivedObserver(this);
        mSubConversationListFragment = new SubConversationListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mSubConversationListFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onReceived(Message message, int i) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RongYunManager.INSTANCE.unRegisterReceivedObserver(this);
    }

    public static class Option extends BaseActivityOption<Option> {

    }

}
