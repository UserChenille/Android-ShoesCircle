package com.zjzf.shoescircleandroid.ui.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.base.baseadapter.BaseRecyclerViewAdapter;
import com.zjzf.shoescircle.lib.base.baseadapter.BaseRecyclerViewHolder;
import com.zjzf.shoescircle.lib.manager.ImageLoaderManager;
import com.zjzf.shoescircle.lib.utils.TimeUtil;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.widget.imageview.RoundedImageView;
import com.zjzf.shoescircle.ui.widget.textview.ExTextView;
import com.zjzf.shoescircle.ui.widget.textview.button.ExButton;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.net.apis.GoodsApis;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ListData;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.model.BaseActivityOption;
import com.zjzf.shoescircleandroid.model.CommentInfo;
import com.zjzf.shoescircleandroid.model.GoodsDetailInfo;
import com.zjzf.shoescircleandroid.model.PopupCommentInfo;
import com.zjzf.shoescircleandroid.widget.popup.PopupCommentList;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * Created by 陈志远 on 2018/8/5.
 */
public class GoodsDetailActivity extends BaseActivity {

    @BindView(R.id.iv_goods)
    ImageView mIvGoods;
    @BindView(R.id.tv_type)
    ExTextView mTvType;
    @BindView(R.id.tv_name)
    ExTextView mTvName;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.rv_sizes)
    RecyclerView mRvSizes;
    @BindView(R.id.iv_logo)
    RoundedImageView mIvLogo;
    @BindView(R.id.tv_shop_name)
    TextView mTvShopName;
    @BindView(R.id.ratingBar)
    RatingBar mRatingBar;
    @BindView(R.id.tv_comment)
    TextView mTvComment;
    @BindView(R.id.btn_contact)
    ExButton mBtnContact;

    GoodsDetailOption mDetailOption;
    GoodsDetailInfo mDetailInfo;
    PopupCommentList mPopupCommentList;

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {
        mDetailOption = getActivityOption(GoodsDetailOption.class);
        if (mDetailOption == null || TextUtils.isEmpty(mDetailOption.getGoodsId())) {
            finish();
            return;
        }

    }

    @Override
    protected void onInitView(View decorView) {
        RetrofitClient.get()
                .create(GoodsApis.class)
                .getGoodsDetail(mDetailOption.getGoodsId())
                .compose(this.<ObjectData<GoodsDetailInfo>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData<GoodsDetailInfo>>() {
                    @Override
                    public void onNext(ObjectData<GoodsDetailInfo> result) {
                        bindViewDetail(result.getData());
                    }
                });
        mPopupCommentList = new PopupCommentList(this);
        mPopupCommentList.setAlignBackground(true)
                .setAlignBackgroundGravity(Gravity.BOTTOM)
                .setPopupGravity(Gravity.TOP);
        mPopupCommentList.setContactButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toContact();
                mPopupCommentList.dismiss();
            }
        });

    }

    private void bindViewDetail(GoodsDetailInfo data) {
        if (data == null) return;
        mDetailInfo = data;
        fetchComment();
        ImageLoaderManager.INSTANCE.loadImage(mIvGoods, data.getPhoto());
        mTvType.setText(data.getFreightNo());
        mTvName.setText(data.getName());
        mTvDate.setText(String.format("发布于  %s", TimeUtil.transferDateFromate(data.getCreateTime(), TimeUtil.YYYYMMDD_SLASH)));
        List<String> sizes = data.getSizeList();
        if (!ToolUtil.isListEmpty(sizes)) {
            mRvSizes.setLayoutManager(new GridLayoutManager(this, 4));
            InnerSizeAdapter adapter = new InnerSizeAdapter(this, sizes);
            mRvSizes.setAdapter(adapter);
        }

        if (data.getMember() != null) {
            GoodsDetailInfo.MemberInfo user = data.getMember();
            ImageLoaderManager.INSTANCE.loadCircleImage(mIvLogo, user.getAvatar());
            mTvShopName.setText(user.getMemName());
            mRatingBar.setRating(user.getEvaluate());
        }

    }

    @OnClick(R.id.btn_contact)
    void toContact() {
        if (mDetailInfo == null || mDetailInfo.getMember() == null) return;
        GoodsDetailInfo.MemberInfo memberInfo = mDetailInfo.getMember();
        Bundle bundle = new Bundle();
        bundle.putParcelable("goodsDetail", mDetailInfo);
        RongIM.getInstance().startConversation(getContext(),
                Conversation.ConversationType.PRIVATE,
                memberInfo.getId(),
                memberInfo.getMemName(),
                bundle);
    }

    @OnClick(R.id.iv_logo)
    void showCommentList() {
        if (mDetailInfo == null) return;
        fetchComment();
        mPopupCommentList.bindData(PopupCommentInfo.createFromGoodsDetailInfo(mDetailInfo));
        mPopupCommentList.showPopupWindow(mBtnContact);
    }

    private void fetchComment() {
        if (mDetailInfo == null) return;
        RetrofitClient.get()
                .create(GoodsApis.class)
                .getCommentList(String.valueOf(mDetailInfo.getMemId()))
                .compose(this.<ListData<CommentInfo>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ListData<CommentInfo>>() {
                    @Override
                    public void onNext(ListData<CommentInfo> result) {
                        if (mPopupCommentList != null) {
                            mPopupCommentList.updateComment(result.getList());
                        }
                        mTvComment.setText(String.format("·%1$s条评论", ToolUtil.isListEmpty(result.getList()) ? 0 : result.getList().size()));
                    }
                });
    }

    private class InnerSizeAdapter extends BaseRecyclerViewAdapter<String> {

        public InnerSizeAdapter(@NonNull Context context, @NonNull List<String> datas) {
            super(context, datas);
        }

        @Override
        protected int getViewType(int position, @NonNull String data) {
            return 0;
        }

        @Override
        protected int getLayoutResId(int viewType) {
            return R.layout.item_goods_detail_size;
        }

        @Override
        protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
            return new InnerViewHolder(rootView, viewType);
        }

        private class InnerViewHolder extends BaseRecyclerViewHolder<String> {

            TextView tvSize;

            public InnerViewHolder(View itemView, int viewType) {
                super(itemView, viewType);
                tvSize = findViewById(R.id.tv_size);
            }

            @Override
            public void onBindData(String data, int position) {
                tvSize.setText(data);
            }
        }
    }

    public static class GoodsDetailOption extends BaseActivityOption<GoodsDetailOption> {
        private String goodsId;

        public String getGoodsId() {
            return goodsId;
        }

        public GoodsDetailOption setGoodsId(String goodsId) {
            this.goodsId = goodsId;
            return this;
        }
    }

}
