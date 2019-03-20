package com.zjzf.shoescircleandroid.ui.main;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.zjzf.shoescircle.lib.interfaces.SimpleCallback;
import com.zjzf.shoescircle.lib.manager.ImageLoaderManager;
import com.zjzf.shoescircle.lib.utils.DecimalUtil;
import com.zjzf.shoescircle.lib.utils.EventBusUtil;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.widget.imageview.RoundedImageView;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.ui.widget.textview.ExTextView;
import com.zjzf.shoescircle.ui.widget.textview.button.ExButton;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.fragment.BaseFragment;
import com.zjzf.shoescircleandroid.base.manager.UserInfoManager;
import com.zjzf.shoescircleandroid.base.net.apis.GoodsApis;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ListData;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;
import com.zjzf.shoescircleandroid.model.GoodsSellInfo;
import com.zjzf.shoescircleandroid.model.UserDetailInfo;
import com.zjzf.shoescircleandroid.model.UserInfo;

import butterknife.BindView;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by 陈志远 on 2018/7/22.
 */
public class MeFragment extends BaseFragment {

    @BindView(R.id.iv_avatar)
    RoundedImageView mIvAvatar;
    @BindView(R.id.btn_edit)
    ExButton mBtnEdit;
    @BindView(R.id.tv_nick)
    ExTextView mTvNick;
    @BindView(R.id.ratingBar)
    MaterialRatingBar mRatingBar;
    @BindView(R.id.progress_level)
    ProgressBar mProgressLevel;
    @BindView(R.id.tv_cur_level)
    ExTextView mTvCurLevel;
    @BindView(R.id.tv_next_level)
    ExTextView mTvNextLevel;
    @BindView(R.id.tv_level_desc)
    ExTextView mTvLevelDesc;
    @BindView(R.id.tv_level_desc_2)
    ExTextView mTvLevelDesc2;
    @BindView(R.id.ll_my_buy)
    LinearLayout mLlMyBuy;
    @BindView(R.id.tv_no_send)
    ExTextView mTvNoSend;
    @BindView(R.id.ll_my_sold)
    LinearLayout mLlMySold;
    @BindView(R.id.tv_verify_state)
    ExTextView mTvVerifyState;
    @BindView(R.id.ll_verify_account)
    LinearLayout mLlVerifyAccount;
    @BindView(R.id.ll_my_feedback)
    LinearLayout mLlMyFeedback;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void loadNetData() {
        updateUserData();
    }


    @Override
    public void onLoginSuccess(UserInfo userInfo) {
        updateUserData();
    }

    @Override
    protected void beforeReturenRootView() {
        super.beforeReturenRootView();
        EventBusUtil.register(this);
    }

    private void updateUserData() {
        UserInfoManager.INSTANCE.getUserInfo()
                .getUserDetailInfo(new SimpleCallback<UserDetailInfo>() {
                    @Override
                    public void onCall(UserDetailInfo data) {
                        bindUserData(data);
                    }
                }, true);
    }

    @Override
    public void onShow() {
        if (isOverRefreshTime()) {
            RetrofitClient.get()
                    .create(GoodsApis.class)
                    .getGoodsSellListInfo(20)
                    .compose(this.<ListData<GoodsSellInfo>>normalSchedulerTransformer())
                    .subscribe(new OnResponseListener<ListData<GoodsSellInfo>>() {
                        @Override
                        public void onNext(ListData<GoodsSellInfo> result) {
                            ViewUtil.setViewsVisible(ToolUtil.isListEmpty(result.getList()) ? View.GONE : View.VISIBLE, mTvNoSend);
                            if (!ToolUtil.isListEmpty(result.getList())) {
                                mTvNoSend.setText(String.format("%1$s个未发货", result.getList().size()));
                            }
                        }
                    });
        }
    }

    private void bindUserData(UserDetailInfo userDetailInfo) {
        UserInfoManager.INSTANCE.getUserInfo().setUserDetailInfo(userDetailInfo);
        if (userDetailInfo == null) {
            ImageLoaderManager.INSTANCE.loadCircleImage(mIvAvatar, null);
            mTvNick.setText("请登录");
            mRatingBar.setRating(0);
            setProgress(0);
            mTvCurLevel.setText("请登录");
            mTvLevelDesc.setText(StringUtil.getString(R.string.vip_desc, "请登录"));
            mTvLevelDesc2.setText(StringUtil.getString(R.string.trade_desc, 0));
            return;
        }
        ImageLoaderManager.INSTANCE.loadCircleImage(mIvAvatar, userDetailInfo.getAvatar());
        mTvNick.setText(userDetailInfo.getMemName());
        setProgress((int) ((userDetailInfo.getScore() * 1.0f / userDetailInfo.getUpgradeScore() * 1.0f) * 100));
        mTvNextLevel.setText(StringUtil.getString(R.string.next_level_tips, userDetailInfo.getUpgradeScore() - userDetailInfo.getScore()));
        if (userDetailInfo.getXqMemlevel() != null) {
            UserDetailInfo.XqMemlevelInfo memlevelInfo = userDetailInfo.getXqMemlevel();
            mRatingBar.setRating(Math.min(memlevelInfo.getLevelNo(), 5));
            mTvCurLevel.setText(memlevelInfo.getName());
            mTvLevelDesc.setText(StringUtil.getString(R.string.vip_desc, memlevelInfo.getName()));
            mTvLevelDesc2.setText(StringUtil.getString(R.string.trade_desc, DecimalUtil.format(memlevelInfo.getFee() * 100)));
        }
    }

    private void setProgress(int progress) {
        ValueAnimator animator = ValueAnimator.ofInt(0, progress);
        animator.setDuration(1200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgressLevel.setProgress((Integer) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    @OnClick(R.id.ll_my_feedback)
    void toFeedback() {
        ActivityLauncher.startToFeedbackActivity(getContext());
    }

    @OnClick(R.id.ll_my_buy)
    void toMyGoodsDemand() {
        ActivityLauncher.startToGoodsDemandActivity(getContext(), null);
    }

    @OnClick(R.id.ll_my_sold)
    void toMyGoodsSell() {
        ActivityLauncher.startToGoodsSellActivity(getContext(), null);
    }

    @OnClick({R.id.ll_user, R.id.btn_edit})
    void toUserEdit() {
        ActivityLauncher.startToUserEditActivity(getContext());
    }

}
