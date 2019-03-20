package com.zjzf.shoescircleandroid.widget.popup;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.base.baseadapter.BaseMultiRecyclerViewHolder;
import com.zjzf.shoescircle.lib.base.baseadapter.LayoutId;
import com.zjzf.shoescircle.lib.base.baseadapter.MultiRecyclerViewAdapter;
import com.zjzf.shoescircle.lib.manager.ImageLoaderManager;
import com.zjzf.shoescircle.lib.utils.TimeUtil;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.widget.imageview.RoundedImageView;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.ui.widget.LoadingView;
import com.zjzf.shoescircle.ui.widget.textview.button.ExButton;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.model.CommentInfo;
import com.zjzf.shoescircleandroid.model.PopupCommentInfo;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 陈志远 on 2019/3/13.
 */
public class PopupCommentList extends BasePopupWindow {
    ImageView ivClose;
    RoundedImageView ivAvatar;
    TextView tvName;
    MaterialRatingBar ratingBar;
    TextView tvTime;
    TextView tvCommentCounts;
    RecyclerView mRvContent;
    ExButton btnContact;
    TextView mTvEmpty;
    LoadingView mLoadingView;
    private MultiRecyclerViewAdapter<CommentInfo> mAdapter;

    public PopupCommentList(Context context) {
        super(context);
        this.ivClose = (ImageView) findViewById(R.id.iv_close);
        this.ivAvatar = (RoundedImageView) findViewById(R.id.iv_avatar);
        this.tvName = (TextView) findViewById(R.id.tv_name);
        this.ratingBar = (MaterialRatingBar) findViewById(R.id.ratingBar);
        this.tvTime = (TextView) findViewById(R.id.tv_time);
        this.tvCommentCounts = (TextView) findViewById(R.id.tv_comment_counts);
        this.btnContact = (ExButton) findViewById(R.id.btn_contact);
        this.mRvContent = findViewById(R.id.rv_content);
        this.mTvEmpty = findViewById(R.id.tv_empty);
        this.mLoadingView = findViewById(R.id.loading_view);
        setBlurBackgroundEnable(true);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(v);
                }
            }
        });
        mAdapter = new MultiRecyclerViewAdapter(context);
        mAdapter.addViewHolder(InnerViewHolder.class, 0);
        mRvContent.setLayoutManager(new LinearLayoutManager(context));
        mRvContent.setAdapter(mAdapter);

    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_user_detail);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0f, 350);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0f, 1f, 350);
    }


    public void updateComment(List<CommentInfo> commentInfos) {
        mLoadingView.stop();
        ViewUtil.setViewsVisible(View.GONE, mLoadingView);
        if (!ToolUtil.isListEmpty(commentInfos)) {
            ViewUtil.setViewsVisible(View.GONE, mTvEmpty);
            ViewUtil.setViewsVisible(View.VISIBLE, mRvContent);
            mAdapter.updateData(commentInfos);
        } else {
            ViewUtil.setViewsVisible(View.GONE, mRvContent);
            ViewUtil.setViewsVisible(View.VISIBLE, mTvEmpty);
        }
        tvCommentCounts.setText(String.format("共%1$s条", ToolUtil.isListEmpty(commentInfos) ? 0 : commentInfos.size()));
    }

    public void bindData(PopupCommentInfo info) {
        if (info == null) return;
        ImageLoaderManager.INSTANCE.loadCircleImage(ivAvatar, info.getAvatar());
        tvName.setText(info.getName());
        ratingBar.setRating(info.getRating());
        tvTime.setText(String.format("使用鞋圈%1$s天，交易过%2$s次", getInterValTime(info.getCreateTime()), info.getTradeNum()));
        if (ToolUtil.isListEmpty(mAdapter.getDatas())) {
            mLoadingView.start();
        } else {
            mLoadingView.stop();
        }
    }

    private String getInterValTime(String createTime) {
        long start = TimeUtil.stringToTimeStamp(createTime);
        long end = System.currentTimeMillis();
        return String.valueOf(TimeUtil.getSubDay(start, end));
    }

    public void setContactBtnVisible(boolean visible) {
        ViewUtil.setViewsVisible(visible ? View.VISIBLE : View.GONE, btnContact);
    }

    @LayoutId(id = R.layout.item_detail_comment)
    private static class InnerViewHolder extends BaseMultiRecyclerViewHolder<CommentInfo> {
        RoundedImageView ivAvatar;
        TextView tvName;
        TextView tvTime;
        TextView tvContent;

        public InnerViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            this.ivAvatar = (RoundedImageView) findViewById(R.id.iv_avatar);
            this.tvName = (TextView) findViewById(R.id.tv_name);
            this.tvTime = (TextView) findViewById(R.id.tv_time);
            this.tvContent = (TextView) findViewById(R.id.tv_content);
        }

        @Override
        public void onBindData(CommentInfo data, int position) {
            CommentInfo.BuyerInfo buyerInfo = data.getBuyer();
            if (buyerInfo != null) {
                ImageLoaderManager.INSTANCE.loadCircleImage(ivAvatar, buyerInfo.getAvatar());
                tvName.setText(buyerInfo.getMemName());
            }
            tvTime.setText(data.getCreateTime());
            tvContent.setText(data.getCommentContent());

        }
    }

    private View.OnClickListener mOnClickListener;


    public PopupCommentList setContactButtonClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        return this;
    }
}
