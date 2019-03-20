package com.zjzf.shoescircleandroid.ui.transaction.scan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.base.baseadapter.BaseRecyclerViewAdapter;
import com.zjzf.shoescircle.lib.base.baseadapter.BaseRecyclerViewHolder;
import com.zjzf.shoescircle.lib.base.baseadapter.OnRecyclerViewItemClickListener;
import com.zjzf.shoescircle.lib.manager.ImageLoaderManager;
import com.zjzf.shoescircle.lib.utils.PinYinUtil;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.ui.widget.EmptyView;
import com.zjzf.shoescircle.ui.widget.GridLayoutSpaceDecoration;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.net.apis.ExpressApis;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.model.express.ExpressCompanyInfo;
import com.zjzf.shoescircleandroid.widget.indexbar.IndexSideBarView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by 陈志远 on 2019/3/11.
 */
public class ExpressCompanySelectActivity extends BaseActivity {
    @BindView(R.id.index_slide_bar)
    IndexSideBarView mIndexSlideBar;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.view_empty)
    EmptyView mViewEmpty;
    @BindView(R.id.content)
    FrameLayout mContent;

    private InnerAdapter mAdapter;

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_express_company_select;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onInitView(View decorView) {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter != null) {
                    return mAdapter.getDatas().get(position).getItemType() == 0 ? 1 : 2;
                }
                return 0;
            }
        });
        mRvContent.setLayoutManager(gridLayoutManager);
        mRvContent.addItemDecoration(new GridLayoutSpaceDecoration(UIHelper.dip2px(8)));
        mAdapter = new InnerAdapter(this);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<ExpressCompanyInfo>() {
            @Override
            public void onItemClick(View v, int position, ExpressCompanyInfo data) {
                if (data.getItemType() == 1) return;
                Intent intent = new Intent();
                intent.putExtra("result", data);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mRvContent.setAdapter(mAdapter);

        mIndexSlideBar.setOnSelectedListener(new IndexSideBarView.OnSelectedListener() {
            @Override
            public void onSelectedListener(String mSlectedTag) {
                if (mAdapter != null) {
                    int pos = mAdapter.getSortPosition(mSlectedTag);
                    if (pos != -1) {
                        gridLayoutManager.scrollToPositionWithOffset(pos, 0);
                    }
                }
            }
        });

        RetrofitClient.get()
                .create(ExpressApis.class)
                .queryExpressCompanyList()
                .compose(this.<ObjectData<List<ExpressCompanyInfo>>>normalSchedulerTransformer())
                .subscribe(new OnResponseListener<ObjectData<List<ExpressCompanyInfo>>>(this, true, true) {
                    @Override
                    public void onNext(ObjectData<List<ExpressCompanyInfo>> result) {
                        bindData(result.getData());
                    }
                });

    }

    private void bindData(List<ExpressCompanyInfo> data) {
        if (ToolUtil.isListEmpty(data)) {
            ViewUtil.setViewsVisible(View.VISIBLE, mViewEmpty);
            ViewUtil.setViewsVisible(View.GONE, mContent);
            return;
        }

        mAdapter.updateData(sortList(data));

    }

    private List<ExpressCompanyInfo> sortList(List<ExpressCompanyInfo> data) {
        HashMap<String, List<ExpressCompanyInfo>> map = new HashMap<>();
        for (ExpressCompanyInfo datum : data) {
            String pinyin = PinYinUtil.getPingYin(datum.getName());
            String letter = pinyin.substring(0, 1).toUpperCase();
            if (!letter.matches("[A-Z]")) {
                letter = "#";
            }
            List<ExpressCompanyInfo> info = map.get(letter);
            if (info == null) {
                info = new ArrayList<>();
                map.put(letter, info);
                info.add(datum);
            } else {
                info.add(datum);
            }
        }
        List<Map.Entry<String, List<ExpressCompanyInfo>>> sortTarget = new ArrayList<>(map.entrySet());
        Collections.sort(sortTarget, new Comparator<Map.Entry<String, List<ExpressCompanyInfo>>>() {
            @Override
            public int compare(Map.Entry<String, List<ExpressCompanyInfo>> o1, Map.Entry<String, List<ExpressCompanyInfo>> o2) {
                if (o1.getKey().equals("@")
                        || o2.getKey().equals("#")) {
                    return 1;
                } else if (o1.getKey().equals("#")
                        || o2.getKey().equals("@")) {
                    return -1;
                } else {
                    return o1.getKey().compareTo(o2.getKey());
                }
            }
        });
        List<ExpressCompanyInfo> result = new ArrayList<>();
        List<String> letters = new ArrayList<>();
        for (Map.Entry<String, List<ExpressCompanyInfo>> entry : sortTarget) {
            ExpressCompanyInfo sortInfo = new ExpressCompanyInfo();
            sortInfo.setSortLetter(entry.getKey());
            letters.add(entry.getKey());
            result.add(sortInfo);
            result.addAll(entry.getValue());
        }
        mIndexSlideBar.setLetters(letters);
        return result;
    }


    private class InnerAdapter extends BaseRecyclerViewAdapter<ExpressCompanyInfo> {

        public InnerAdapter(@NonNull Context context) {
            super(context);
        }

        @Override
        protected int getViewType(int position, @NonNull ExpressCompanyInfo data) {
            return data.getItemType();
        }

        @Override
        protected int getLayoutResId(int viewType) {
            return viewType == 0 ? R.layout.item_express_selected : R.layout.item_express_sort_title;
        }

        public int getSortPosition(String letter) {
            for (int i = 0; i < getDatas().size(); i++) {
                if (TextUtils.equals(getDatas().get(i).getSortLetter(), letter)) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
            return viewType == 0 ? new InnerContentViewHolder(rootView, viewType) : new InnerSortTitleViewHolder(rootView, viewType);
        }

        class InnerSortTitleViewHolder extends BaseRecyclerViewHolder<ExpressCompanyInfo> {
            TextView mTvSortTitle;

            public InnerSortTitleViewHolder(View itemView, int viewType) {
                super(itemView, viewType);
                mTvSortTitle = findViewById(R.id.tv_sort_letter);
            }

            @Override
            public void onBindData(ExpressCompanyInfo data, int position) {
                mTvSortTitle.setText(data.getSortLetter());
            }
        }

        class InnerContentViewHolder extends BaseRecyclerViewHolder<ExpressCompanyInfo> {
            ImageView mIcon;
            TextView mTvName;

            public InnerContentViewHolder(View itemView, int viewType) {
                super(itemView, viewType);
                mIcon = findViewById(R.id.iv_express_icon);
                mTvName = findViewById(R.id.tv_express_company_name);
            }

            @Override
            public void onBindData(ExpressCompanyInfo data, int position) {
                ImageLoaderManager.INSTANCE.loadImage(mIcon, data.getPicture(), R.drawable.ic_express);
                mTvName.setText(data.getNameCn());
            }
        }
    }
}
