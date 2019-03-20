package com.zjzf.shoescircleandroid.ui.main;

import android.net.Uri;

import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.fragment.BaseFragment;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;


/**
 * Created by 陈志远 on 2018/7/22.
 */
public class MsgFragment extends BaseFragment {
    ConversationListFragment mConversationListFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void initViews() {
        if (mConversationListFragment == null) {
            mConversationListFragment = new ConversationListFragment();
            Uri uri = Uri.parse("rong://" + getContext().getPackageName()).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话，该会话聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话，该会话非聚合显示
                    .build();
            mConversationListFragment.setUri(uri);
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mConversationListFragment)
                    .commitAllowingStateLoss();
        }

    }

    @Override
    protected void loadNetData() {

    }
}
