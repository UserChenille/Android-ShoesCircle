package com.zjzf.shoescircleandroid.ui.me.fragments.goods.demanad;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.base.baseadapter.BaseMultiRecyclerViewHolder;
import com.zjzf.shoescircle.lib.base.baseadapter.LayoutId;
import com.zjzf.shoescircle.lib.base.baseadapter.MultiRecyclerViewAdapter;
import com.zjzf.shoescircle.lib.manager.ImageLoaderManager;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.lib.widget.imageview.RoundedImageView;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.net.apis.GoodsApis;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ListData;
import com.zjzf.shoescircleandroid.model.mygoods.GoodsWaitPayInfo;
import com.zjzf.shoescircleandroid.ui.me.fragments.BaseMyGoodsFragment;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

/**
 * Created by 陈志远 on 2018/11/24.
 * <p>
 * 已付款
 */
public class MyGoodsDemanadHasPayFragment extends BaseMyGoodsFragment {
    private MultiRecyclerViewAdapter<GoodsWaitPayInfo> mAdapter;

    @Override
    protected RecyclerView.Adapter onInitAdapter() {
        mAdapter = new MultiRecyclerViewAdapter<>(getContext())
                .addViewHolder(InnerViewHolder.class, 0);
        mAdapter.bindWith(this);
        return mAdapter;
    }

    @Override
    protected void onInitRecyclerView(RecyclerView recyclerView) {
        super.onInitRecyclerView(recyclerView);
        mRvContent.setLoadMoreEnable(false);
        recyclerView.setItemAnimator(new LandingAnimator());
    }

    @Override
    protected void onRefresh(int page, int rows) {
        RetrofitClient.get()
                .create(GoodsApis.class)
                .getGoodsWaitPayList(20)
                .compose(this.<ListData<GoodsWaitPayInfo>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ListData<GoodsWaitPayInfo>>() {
                    @Override
                    public void onNext(ListData<GoodsWaitPayInfo> result) {
                        mAdapter.updateData(result.getList());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        compelete();
                    }
                });
    }

    @Override
    protected void onLoadMore(int page, int rows) {

    }

    @Override
    public String getTitle() {
        return "已付款";
    }

    @LayoutId(id = R.layout.item_fragment_my_goods_demand_has_pay)
    private class InnerViewHolder extends BaseMultiRecyclerViewHolder<GoodsWaitPayInfo> {
        RoundedImageView ivPhoto;
        TextView tvName;
        TextView tvGoodsNo;
        TextView tvGoodsSize;
        TextView tvPrice;
        TextView tvDesc;
        TextView tvCheckExpress;
        TextView tvComment;

        public InnerViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            this.ivPhoto = (RoundedImageView) findViewById(R.id.iv_photo);
            this.tvName = findViewById(R.id.tv_name);
            this.tvGoodsNo = findViewById(R.id.tv_goods_no);
            this.tvGoodsSize = findViewById(R.id.tv_goods_size);
            this.tvPrice = findViewById(R.id.tv_price);
            this.tvDesc = findViewById(R.id.tv_desc);
            this.tvCheckExpress = findViewById(R.id.tv_check_express);
            this.tvComment = findViewById(R.id.tv_comment);
        }

        @Override
        public void onBindData(final GoodsWaitPayInfo data, final int position) {
            ImageLoaderManager.INSTANCE.loadRoundImage(ivPhoto, data.getPhoto(), UIHelper.dip2px(6));
            tvName.setText(data.getName());
            tvGoodsNo.setText("货号：" + data.getFreightNo());
            tvGoodsSize.setText("尺码：" + data.getSize());
            if (data.getOrderAmount() > 0) {
                tvPrice.setVisibility(View.VISIBLE);
                tvPrice.setText(StringUtil.getString(R.string.money_value, data.getOrderAmount()));
            } else {
                tvPrice.setVisibility(View.GONE);
            }
            ViewUtil.setViewsVisible(data.isCommented() ? View.GONE : View.VISIBLE, tvComment);
            tvCheckExpress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            tvComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            tvDesc.setText(String.format("收货地址：\n%s", data.getAddressInfo()));
        }
    }
}
