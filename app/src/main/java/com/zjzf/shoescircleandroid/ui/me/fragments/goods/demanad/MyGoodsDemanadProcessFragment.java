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
import com.zjzf.shoescircle.ui.widget.textview.ExTextView;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.net.apis.GoodsApis;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ListData;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.model.mygoods.GoodsDemandProcessInfo;
import com.zjzf.shoescircleandroid.ui.me.fragments.BaseMyGoodsFragment;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

/**
 * Created by 陈志远 on 2018/11/24.
 * <p>
 * 求货中
 */
public class MyGoodsDemanadProcessFragment extends BaseMyGoodsFragment {

    private MultiRecyclerViewAdapter<GoodsDemandProcessInfo> mAdapter;

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
        recyclerView.setItemAnimator(new LandingAnimator());
    }

    @Override
    protected void onRefresh(int page, int rows) {
        RetrofitClient.get()
                .create(GoodsApis.class)
                .getGoodsDemandProcessList(page, rows)
                .compose(this.<ListData<GoodsDemandProcessInfo>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ListData<GoodsDemandProcessInfo>>() {
                    @Override
                    public void onNext(ListData<GoodsDemandProcessInfo> result) {
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
        RetrofitClient.get()
                .create(GoodsApis.class)
                .getGoodsDemandProcessList(page, rows)
                .compose(this.<ListData<GoodsDemandProcessInfo>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ListData<GoodsDemandProcessInfo>>() {
                    @Override
                    public void onNext(ListData<GoodsDemandProcessInfo> result) {
                        mAdapter.addMore(result.getList());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        compelete();
                    }
                });
    }


    @Override
    public String getTitle() {
        return "求货中";
    }

    @LayoutId(id = R.layout.item_fragment_my_goods_demand_process)
    private class InnerViewHolder extends BaseMultiRecyclerViewHolder<GoodsDemandProcessInfo> {
        RoundedImageView ivPhoto;
        TextView tvName;
        TextView tvGoodsNo;
        TextView tvGoodsSize;
        TextView tvPrice;
        TextView tvDesc;
        ExTextView tvUnderCarriage;

        public InnerViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            this.ivPhoto = (RoundedImageView) findViewById(R.id.iv_photo);
            this.tvName = (TextView) findViewById(R.id.tv_name);
            this.tvGoodsNo = (TextView) findViewById(R.id.tv_goods_no);
            this.tvGoodsSize = (TextView) findViewById(R.id.tv_goods_size);
            this.tvPrice = (TextView) findViewById(R.id.tv_price);
            this.tvDesc = (TextView) findViewById(R.id.tv_desc);
            this.tvUnderCarriage = (ExTextView) findViewById(R.id.tv_under_carriage);
        }

        @Override
        public void onBindData(final GoodsDemandProcessInfo data, final int position) {
            ImageLoaderManager.INSTANCE.loadRoundImage(ivPhoto, data.getPhoto(), UIHelper.dip2px(6));
            tvName.setText(data.getName());
            tvGoodsNo.setText("货号：" + data.getFreightNo());
            tvGoodsSize.setText("尺码：" + data.getSize());
            if (data.getPrice() > 0) {
                tvPrice.setVisibility(View.VISIBLE);
                tvPrice.setText(StringUtil.getString(R.string.money_value, data.getPrice()));
            } else {
                tvPrice.setVisibility(View.GONE);
            }

            tvUnderCarriage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoading();
                    RetrofitClient.get()
                            .create(GoodsApis.class)
                            .offSell(data.getId())
                            .compose(MyGoodsDemanadProcessFragment.this.<ObjectData>normalSchedulerTransformer())
                            .subscribe(new OnResponseListener<ObjectData>() {
                                @Override
                                public void onNext(ObjectData result) {
                                    mAdapter.remove(data);
                                }

                                @Override
                                public void onComplete() {
                                    dismissLoading();
                                    super.onComplete();
                                }
                            });
                }
            });

        }

    }
}
