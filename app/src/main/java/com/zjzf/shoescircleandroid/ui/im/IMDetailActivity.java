package com.zjzf.shoescircleandroid.ui.im;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.zjzf.shoescircle.lib.interfaces.SimpleCallback;
import com.zjzf.shoescircle.lib.utils.EventBusUtil;
import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.im.IMReceiveObserver;
import com.zjzf.shoescircleandroid.base.manager.RongYunManager;
import com.zjzf.shoescircleandroid.base.manager.UserInfoManager;
import com.zjzf.shoescircleandroid.base.net.apis.GoodsApis;
import com.zjzf.shoescircleandroid.base.net.apis.UserApis;
import com.zjzf.shoescircleandroid.base.net.client.HandleServerApiErrorAction;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ListData;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.model.BaseActivityOption;
import com.zjzf.shoescircleandroid.model.CommentInfo;
import com.zjzf.shoescircleandroid.model.PopupCommentInfo;
import com.zjzf.shoescircleandroid.model.UserDetailInfo;
import com.zjzf.shoescircleandroid.model.event.RemoveUnreadMessageCountEvent;
import com.zjzf.shoescircleandroid.viewmodel.IMViewModel;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by 陈志远 on 2018/11/25.
 */
public class IMDetailActivity extends BaseActivity implements IMReceiveObserver {
    ConversationFragment mConversationFragment;
    String targetUserId;
    String title;

    IMViewModel mIMViewModel;

    @Override
    public void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            targetUserId = uri.getQueryParameter("targetId");
            title = uri.getQueryParameter("title");
        }
        mIMViewModel = ViewModelProviders.of(this).get(IMViewModel.class);
        loadData();
    }

    private void loadData() {
        if (TextUtils.isEmpty(targetUserId)) return;
        RetrofitClient.get()
                .create(UserApis.class)
                .getUserById(targetUserId)
                .doOnNext(new HandleServerApiErrorAction<>())
                .compose(this.<ObjectData<UserDetailInfo>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData<UserDetailInfo>>() {
                    @Override
                    public void onNext(ObjectData<UserDetailInfo> result) {
                        if (result.getData() == null) return;
                        mIMViewModel.setOtherCommentInfo(createFromUsetDetail(result.getData()));
                    }
                });
        UserInfoManager.INSTANCE.getUserInfo().getUserDetailInfo(new SimpleCallback<UserDetailInfo>() {
            @Override
            public void onCall(UserDetailInfo data) {
                mIMViewModel.setMyCommentInfo(createFromUsetDetail(data));
            }
        });
        RetrofitClient.get()
                .create(GoodsApis.class)
                .getCommentList(targetUserId)
                .compose(this.<ListData<CommentInfo>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ListData<CommentInfo>>() {
                    @Override
                    public void onNext(ListData<CommentInfo> result) {
                        mIMViewModel.setCommentInfos(result.getList());
                    }
                });
    }

    PopupCommentInfo createFromUsetDetail(UserDetailInfo info) {
        PopupCommentInfo result = new PopupCommentInfo();
        if (info != null) {
            result.setName(info.getMemName());
            result.setAvatar(info.getAvatar());
            result.setCreateTime(info.getCreateTime());
            result.setRating(info.getEvaluate());
        }
        return result;
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
        if (StringUtil.noEmpty(title)) {
            setTitleText(title);
        }
        RongYunManager.INSTANCE.registerReceivedObserver(this);
        mConversationFragment = new ConversationFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mConversationFragment)
                .commitAllowingStateLoss();
        RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, targetUserId, new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                clearUnread(integer);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogHelper.trace(LogHelper.e, errorCode.getMessage());
            }
        });

    }

    private void clearUnread(final Integer integer) {
        RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, targetUserId, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (integer != null) {
                    EventBusUtil.post(new RemoveUnreadMessageCountEvent(integer));
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogHelper.trace(LogHelper.e, errorCode.getMessage());
            }
        });
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
