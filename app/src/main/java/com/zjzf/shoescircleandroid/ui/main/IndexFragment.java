package com.zjzf.shoescircleandroid.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.base.baseadapter.BaseMultiRecyclerViewHolder;
import com.zjzf.shoescircle.lib.base.baseadapter.LayoutId;
import com.zjzf.shoescircle.lib.base.baseadapter.MultiRecyclerViewAdapter;
import com.zjzf.shoescircle.lib.base.baseadapter.OnRecyclerViewItemClickListener;
import com.zjzf.shoescircle.lib.manager.ImageLoaderManager;
import com.zjzf.shoescircle.lib.utils.EventBusUtil;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.widget.pullrecyclerview.PullRecyclerView;
import com.zjzf.shoescircle.ui.widget.pullrecyclerview.interfaces.OnRefreshListener;
import com.zjzf.shoescircle.ui.widget.textview.button.ExButton;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.fragment.BaseFragment;
import com.zjzf.shoescircleandroid.base.net.apis.GoodsApis;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;
import com.zjzf.shoescircleandroid.model.GoodsListInfo;
import com.zjzf.shoescircleandroid.model.event.RefreshEvent;
import com.zjzf.shoescircleandroid.ui.goods.GoodsDetailActivity;
import com.zjzf.shoescircleandroid.utils.LevelUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by 陈志远 on 2018/7/22.
 */
public class IndexFragment extends BaseFragment {
    @BindView(R.id.btn_search)
    ExButton mBtnSearch;
    @BindView(R.id.rv_content)
    PullRecyclerView mRvContent;
    @BindView(R.id.iv_post)
    ImageView mIvPost;

    private MultiRecyclerViewAdapter mAdapter;

    @Subscribe
    public void onEvent(RefreshEvent event) {
        if (event.contains(this.getClass())) {
            if (mRvContent != null) {
                mRvContent.manualRefresh();
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    protected void initViews() {
        EventBusUtil.register(this);
        mAdapter = new MultiRecyclerViewAdapter(getContext(), new ArrayList());
        mAdapter.addViewHolder(InnerViewHolder.class, 0)
                .bindWith(this);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position, Object data) {
                if (data instanceof GoodsListInfo.RecordsBean) {
                    ActivityLauncher.startToGoodsDetailActivity(getContext(),
                            new GoodsDetailActivity.GoodsDetailOption()
                                    .setGoodsId(String.valueOf(((GoodsListInfo.RecordsBean) data).getId())));
                }
            }
        });
        mRvContent.setAdapter(mAdapter);
        mRvContent.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefreshing(PtrFrameLayout frame, RecyclerView recyclerView, int page, int rows) {
                loadData(page);
            }

            @Override
            public void onLoadMore(PtrFrameLayout frame, RecyclerView recyclerView, int page, int rows) {
                loadData(page);
            }
        });
        mRvContent.manualRefresh();
        mRvContent.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                togglePostApply(dy <= 0);
            }
        });
    }

    private volatile boolean isPlayingAnim;

    private void togglePostApply(boolean show) {
        if (isPlayingAnim) return;
        mIvPost.animate()
                .translationX(show ? 0f : mIvPost.getWidth())
                .setDuration(500)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isPlayingAnim = false;
                        mIvPost.animate()
                                .setListener(null);
                    }


                    @Override
                    public void onAnimationStart(Animator animation) {
                        isPlayingAnim = true;
                    }
                }).start();

    }

    void loadData(final int page) {
        RetrofitClient.get()
                .create(GoodsApis.class)
                .getGoodsList(page, 10)
                .compose(this.<ObjectData<GoodsListInfo>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData<GoodsListInfo>>() {
                    @Override
                    public void onNext(ObjectData<GoodsListInfo> result) {
                        if (result.getData() != null) {
                            if (page == 1) {
                                mAdapter.updateData(result.getData().getRecords());
                            } else {
                                mAdapter.addMore(result.getData().getRecords());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mRvContent.compelete();
                    }
                });

    }

    @Override
    protected void loadNetData() {

    }

    @OnClick(R.id.iv_post)
    void toPost() {
        ActivityLauncher.startToPostGoodsActivity(getContext());
    }

    @OnClick(R.id.btn_search)
    void search() {
        ActivityLauncher.startToSearchActivity(getContext());
    }

    @Override
    public void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    @LayoutId(id = R.layout.item_index_goods)
    static class InnerViewHolder extends BaseMultiRecyclerViewHolder<GoodsListInfo.RecordsBean> {
        ImageView ivItem;
        TextView tvName;
        TextView tvShoesName;
        TextView tvSize;
        TextView tvPrice;
        ImageView ivLevel;
        ImageView ivAvatar;
        TextView tvShopName;
        TextView tvShopTime;

        public InnerViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            this.ivItem = findViewById(R.id.iv_item);
            this.tvName = findViewById(R.id.tv_name);
            this.tvShoesName = findViewById(R.id.tv_shoes_name);
            this.tvSize = findViewById(R.id.tv_size);
            this.tvPrice = findViewById(R.id.tv_price);
            this.ivLevel = findViewById(R.id.iv_level);
            this.ivAvatar = findViewById(R.id.iv_avatar);
            this.tvShopName = findViewById(R.id.tv_shop_name);
            this.tvShopTime = findViewById(R.id.tv_shop_time);
        }

        @Override
        public void onBindData(GoodsListInfo.RecordsBean data, int position) {
            ImageLoaderManager.INSTANCE.loadRoundImage(ivItem, data.getPhoto(), UIHelper.dip2px(6));
            tvName.setText(data.getFreightNo());
            tvShoesName.setText(data.getName());
            tvSize.setText(String.format("尺码：%1$s", StringUtil.ellipsizeText(data.getSize(), StringUtil.TYPE_END, 6)));
            if (data.getPrice() > 0) {
                tvPrice.setTextColor(UIHelper.getColor(R.color.price));
                tvPrice.setText(StringUtil.getString(R.string.money_value, data.getPrice()));
            } else {
                tvPrice.setTextColor(UIHelper.getColor(R.color.text_color_3));
                tvPrice.setText("价格待商榷");
            }
            tvShopTime.setText(data.getCreateTime());
            if (data.getMember() != null) {
                ImageLoaderManager.INSTANCE.loadCircleImage(ivAvatar, data.getMember().getAvatar());
                ivLevel.setImageResource(LevelUtil.getLevelIconResource(data.getMember().getLevel()));
                tvShopName.setText(data.getMember().getMemName());
            } else {
                ImageLoaderManager.INSTANCE.loadCircleImage(ivAvatar, data.getMember().getAvatar());
                ivLevel.setImageResource(LevelUtil.getLevelIconResource(1));
                tvShopName.setText("unknow");
            }
        }
    }
}
