package com.zjzf.shoescircleandroid.ui.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.base.baseadapter.BaseListAdapter;
import com.zjzf.shoescircle.lib.base.baseadapter.BaseListViewHolder;
import com.zjzf.shoescircle.lib.base.baseadapter.BaseMultiRecyclerViewHolder;
import com.zjzf.shoescircle.lib.base.baseadapter.LayoutId;
import com.zjzf.shoescircle.lib.base.baseadapter.MultiRecyclerViewAdapter;
import com.zjzf.shoescircle.lib.base.baseadapter.OnRecyclerViewItemClickListener;
import com.zjzf.shoescircle.lib.helper.RxHelper;
import com.zjzf.shoescircle.lib.helper.rx.base.RxCall;
import com.zjzf.shoescircle.lib.manager.ImageLoaderManager;
import com.zjzf.shoescircle.lib.tools.gson.GsonUtil;
import com.zjzf.shoescircle.lib.utils.KeyBoardUtil;
import com.zjzf.shoescircle.lib.utils.MultiSpanUtil;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.widget.pullrecyclerview.PullRecyclerView;
import com.zjzf.shoescircle.ui.widget.pullrecyclerview.config.Mode;
import com.zjzf.shoescircle.ui.widget.textview.ExTextView;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.AppSettingManager;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.net.apis.DefaultApis;
import com.zjzf.shoescircleandroid.base.net.apis.GoodsApis;
import com.zjzf.shoescircleandroid.base.net.client.ApiException;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ListData;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;
import com.zjzf.shoescircleandroid.model.SearchResultInfo;
import com.zjzf.shoescircleandroid.model.SingleStringData;
import com.zjzf.shoescircleandroid.ui.goods.GoodsDetailActivity;
import com.zjzf.shoescircleandroid.utils.LevelUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 陈志远 on 2018/10/16.
 */
public class SearchGoodsActivity extends BaseActivity {
    @BindView(R.id.ed_search)
    AutoCompleteTextView mEdSearch;
    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.rv_content)
    PullRecyclerView mRvContent;
    @BindView(R.id.tv_loading)
    ExTextView tvLoading;

    private SearchAutoAdapter mAutoAdapter;
    private MultiRecyclerViewAdapter<SearchResultInfo> searchAdapter;

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {

    }


    @Override
    protected void onInitView(View decorView) {
        List<String> historySearch = GsonUtil.INSTANCE.toList(AppSettingManager.loadString(AppSettingManager.SEARCH_HISTORY, null), String.class);
        if (historySearch == null) {
            historySearch = new ArrayList<>();
        }
        mAutoAdapter = new SearchAutoAdapter(this, historySearch);
        mEdSearch.setAdapter(mAutoAdapter);

        mRvContent.setMode(Mode.NONE);
        searchAdapter = new MultiRecyclerViewAdapter<>(this);
        searchAdapter.addViewHolder(SearchResultViewHolder.class, 0);
        searchAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<SearchResultInfo>() {
            @Override
            public void onItemClick(View v, int position, SearchResultInfo data) {
                ActivityLauncher.startToGoodsDetailActivity(getContext(),
                        new GoodsDetailActivity.GoodsDetailOption()
                                .setGoodsId(String.valueOf(data.getId())));
            }
        });
        mRvContent.setAdapter(searchAdapter);


        KeyBoardUtil.open(mEdSearch);
        RxHelper.debounceListenEdittext(mEdSearch, 1000, new RxCall<String>() {
            @Override
            public void onCall(String data) {
                search(data);
            }
        });
    }

    private void search(final String data) {
        RetrofitClient.get()
                .create(DefaultApis.class)
                .autoSearch(data)
                .compose(this.<ObjectData<List<SingleStringData>>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData<List<SingleStringData>>>() {
                    @Override
                    public void onNext(ObjectData<List<SingleStringData>> result) {
                        if (!ToolUtil.isListEmpty(result.getData())) {
                            mAutoAdapter.cacheList.clear();
                            List<String> keys = new ArrayList<>();
                            if (!mAutoAdapter.cacheList.contains(data)) {
                                mAutoAdapter.cacheList.add(0, data);
                                if (mAutoAdapter.cacheList.size() > 100) {
                                    for (int i = 100; i < mAutoAdapter.cacheList.size(); i++) {
                                        mAutoAdapter.cacheList.remove(i);
                                    }
                                }
                            }
                            for (SingleStringData datum : result.getData()) {
                                keys.add(datum.getResult());
                            }
                            mAutoAdapter.updateData(keys);
                        }
                    }
                });

        tvLoading.setLoadingText("正在搜索...", 2500);
        tvLoading.setVisibility(View.VISIBLE);
        RetrofitClient.get()
                .create(GoodsApis.class)
                .searchGoodsList(data)
                .compose(this.<ListData<SearchResultInfo>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ListData<SearchResultInfo>>(false) {
                    @Override
                    public void onNext(ListData<SearchResultInfo> result) {
                        searchAdapter.updateData(result.getData().getList());
                        tvLoading.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(ApiException e, int code, String message) {
                        searchAdapter.updateData(null);
                        tvLoading.setVisibility(View.INVISIBLE);
                    }
                });
    }


    private class SearchAutoAdapter extends BaseListAdapter<String, SearchAutoAdapter.InnerViewHolder> implements Filterable {

        private Filter mFilter;
        private int highLightColor = UIHelper.getColor(R.color.common_red);
        private List<String> cacheList = new ArrayList<>();

        public SearchAutoAdapter(Context context, @Nullable List<String> datas) {
            super(context, datas);
            if (datas != null) {
                cacheList.addAll(datas);
            }
        }

        @Override
        public int getCount() {
            return Math.min(10, super.getCount());
        }

        @Override
        public int getLayoutRes(int viewType) {
            return R.layout.item_search_history;
        }

        @Override
        public BaseListViewHolder initViewHolder(int viewType) {
            return new InnerViewHolder();
        }

        @Override
        public void onBindView(int position, final String data, BaseListViewHolder holder) {
            InnerViewHolder innerViewHolder = (InnerViewHolder) holder;
            String key = mEdSearch.getText().toString().toUpperCase();
            MultiSpanUtil.create(data)
                    .append(key).setTextColor(highLightColor)
                    .into(innerViewHolder.content);
            innerViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDatas().remove(data);
                    cacheList.remove(data);
                    saveSearchHistory();
                    notifyDataSetChanged();
                }
            });

        }

        @Override
        public Filter getFilter() {
            if (mFilter == null) {
                mFilter = new InnerFilter();
            }
            return mFilter;
        }

        class InnerViewHolder implements BaseListViewHolder {

            TextView content;
            ImageView delete;

            @Override
            public void onInFlate(View rootView) {
                content = rootView.findViewById(R.id.tv_content);
                delete = rootView.findViewById(R.id.iv_delete);
            }
        }

        class InnerFilter extends Filter {
            List<String> mFilterList = new ArrayList<>();

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults results = new FilterResults();
                if (!TextUtils.isEmpty(constraint)) {
                    mFilterList.clear();
                    for (String search : cacheList) {
                        if (search.toLowerCase().contains(constraint.toString().trim().toLowerCase())) {
                            mFilterList.add(search);
                        }
                    }
                    results.values = mFilterList;
                    results.count = mFilterList.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    updateData((List<String>) results.values);
                }
            }
        }

    }

    @LayoutId(id = R.layout.item_search_goods)
    static class SearchResultViewHolder extends BaseMultiRecyclerViewHolder<SearchResultInfo> {
        ImageView ivItem;
        TextView tvName;
        TextView tvShoesName;
        TextView tvSize;
        TextView tvPrice;
        ImageView ivLevel;
        ImageView ivAvatar;
        TextView tvShopName;
        TextView tvShopTime;

        public SearchResultViewHolder(View itemView, int viewType) {
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
        public void onBindData(SearchResultInfo data, int position) {
            ImageLoaderManager.INSTANCE.loadImage(ivItem, data.getPhoto());
            tvName.setText(data.getName());
            String itemNo = "货号：" + data.getFreightNo();
            MultiSpanUtil.create(itemNo)
                    .append("货号：").setTextColorFromRes(R.color.common_black).setTextType(Typeface.DEFAULT_BOLD)
                    .into(tvShoesName);
            tvSize.setText(String.format("尺码：%1$s", StringUtil.ellipsizeText(data.getSize(), StringUtil.TYPE_END, 6)));

            if (data.getPrice() > 0) {
                tvPrice.setText(StringUtil.getString(R.string.money_value, data.getPrice()));
            } else {
                tvPrice.setText("");
            }
            if (data.getMember() != null) {
                ImageLoaderManager.INSTANCE.loadCircleImage(ivAvatar, data.getMember().getAvatar());
                ivLevel.setImageResource(LevelUtil.getLevelIconResource(data.getMember().getLevel()));
                tvShopName.setText(data.getMember().getMemName());
            } else {
                ImageLoaderManager.INSTANCE.loadCircleImage(ivAvatar, data.getMember().getAvatar());
                ivLevel.setImageResource(LevelUtil.getLevelIconResource(1));
                tvShopName.setText("unknow");
            }
            tvPrice.setText(StringUtil.getString(R.string.money_value, data.getPrice()));
        }

    }

    @Override
    public void finish() {
        saveSearchHistory();
        super.finish();
    }

    private void saveSearchHistory() {
        if (mAutoAdapter == null) return;
        AppSettingManager.saveString(AppSettingManager.SEARCH_HISTORY, GsonUtil.INSTANCE.toString(mAutoAdapter.cacheList));

    }

    @OnClick(R.id.iv_close)
    void close() {
        finish();
    }
}
