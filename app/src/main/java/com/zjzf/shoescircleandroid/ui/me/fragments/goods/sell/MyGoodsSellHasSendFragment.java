package com.zjzf.shoescircleandroid.ui.me.fragments.goods.sell;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.base.baseadapter.BaseMultiRecyclerViewHolder;
import com.zjzf.shoescircle.lib.base.baseadapter.LayoutId;
import com.zjzf.shoescircle.lib.base.baseadapter.MultiRecyclerViewAdapter;
import com.zjzf.shoescircle.lib.base.baseadapter.OnRecyclerViewItemClickListener;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.ui.widget.pullrecyclerview.config.Mode;
import com.zjzf.shoescircle.ui.widget.textview.ExTextView;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.net.apis.GoodsApis;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ListData;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;
import com.zjzf.shoescircleandroid.model.GoodsSellInfo;
import com.zjzf.shoescircleandroid.ui.me.fragments.BaseMyGoodsFragment;
import com.zjzf.shoescircleandroid.ui.transaction.TransactionDetailActivity;

/**
 * Created by 陈志远 on 2018/11/24.
 * <p>
 * 已发货
 */
public class MyGoodsSellHasSendFragment extends BaseMyGoodsFragment {
    private MultiRecyclerViewAdapter<GoodsSellInfo> mAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        getRvContent().setMode(Mode.REFRESH);
    }

    @Override
    protected RecyclerView.Adapter onInitAdapter() {
        mAdapter = new MultiRecyclerViewAdapter<>(getContext())
                .addViewHolder(InnerViewHolder.class, 0);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<GoodsSellInfo>() {
            @Override
            public void onItemClick(View v, int position, GoodsSellInfo data) {
                ActivityLauncher.startToTransactionDetailActivity(getContext(), new TransactionDetailActivity.Option()
                        .setOrderId(data.getId()));
            }
        });
        return mAdapter;
    }


    @Override
    protected void onRefresh(int page, int rows) {
        RetrofitClient.get()
                .create(GoodsApis.class)
                .getGoodsSellListInfo(30)
                .compose(this.<ListData<GoodsSellInfo>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ListData<GoodsSellInfo>>() {
                    @Override
                    public void onNext(ListData<GoodsSellInfo> result) {
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
        return "已发货";
    }

    @LayoutId(id = R.layout.item_fragment_my_goods_sell_has_send)
    private static class InnerViewHolder extends BaseMultiRecyclerViewHolder<GoodsSellInfo> {
        TextView tvName;
        TextView tvGoodsSize;
        TextView tvGoodsAmount;
        TextView tvPrice;
        TextView tvDesc;
        TextView tvCheckLogistics;

        public InnerViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            this.tvName = (TextView) findViewById(R.id.tv_name);
            this.tvGoodsSize = (TextView) findViewById(R.id.tv_goods_size);
            this.tvGoodsAmount = (TextView) findViewById(R.id.tv_goods_amount);
            this.tvPrice = (TextView) findViewById(R.id.tv_price);
            this.tvDesc = (TextView) findViewById(R.id.tv_desc);
            this.tvCheckLogistics = (ExTextView) findViewById(R.id.tv_check_logistics);
        }

        @Override
        public void onBindData(GoodsSellInfo data, int position) {
            tvName.setText(data.getFreightNo());
            tvGoodsSize.setText("尺码：" + data.getSize());
            tvGoodsAmount.setText("数量：" + data.getNum());
            if (data.getOrderAmount() > 0) {
                tvPrice.setVisibility(View.VISIBLE);
                tvPrice.setText(StringUtil.getString(R.string.money_value, data.getOrderAmount()));
            } else {
                tvPrice.setVisibility(View.GONE);
            }
        }

    }
}
