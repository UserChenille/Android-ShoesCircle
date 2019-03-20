package com.zjzf.shoescircleandroid.widget.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.base.baseadapter.BaseRecyclerViewAdapter;
import com.zjzf.shoescircle.lib.base.baseadapter.BaseRecyclerViewHolder;
import com.zjzf.shoescircle.lib.base.baseadapter.OnRecyclerViewItemClickListener;
import com.zjzf.shoescircle.lib.manager.ImageLoaderManager;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.widget.GridLayoutSpaceDecoration;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.model.express.ExpressCompanyInfo;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 陈志远 on 2019/3/13.
 */
public class PopupRecommendExpress extends BasePopupWindow {

    private RecyclerView mRvContent;
    private InnerAdapter mAdapter;
    private OnItemSelectListener mOnItemSelectListener;

    public PopupRecommendExpress(Context context) {
        super(context);
        mRvContent = findViewById(R.id.rv_content);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRvContent.setLayoutManager(gridLayoutManager);
        mRvContent.addItemDecoration(new GridLayoutSpaceDecoration(UIHelper.dip2px(8)));
        mAdapter = new InnerAdapter(getContext());
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<ExpressCompanyInfo>() {
            @Override
            public void onItemClick(View v, int position, ExpressCompanyInfo data) {
                if (mOnItemSelectListener != null) {
                    mOnItemSelectListener.onItemSelected(data);
                }
                dismiss();
            }
        });
        mRvContent.setAdapter(mAdapter);
        setClipChildren(false)
                .setBlurBackgroundEnable(true);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_recommend_express);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getScaleAnimation(1.5f, 1f, 1.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }

    public void showPopupWindow(List<ExpressCompanyInfo> expressCompanyInfos) {
        if (ToolUtil.isListEmpty(expressCompanyInfos)) {
            return;
        }
        mAdapter.updateData(expressCompanyInfos);
        showPopupWindow();
    }

    private class InnerAdapter extends BaseRecyclerViewAdapter<ExpressCompanyInfo> {

        InnerAdapter(@NonNull Context context) {
            super(context);
        }

        @Override
        protected int getViewType(int position, @NonNull ExpressCompanyInfo data) {
            return data.getItemType();
        }

        @Override
        protected int getLayoutResId(int viewType) {
            return R.layout.item_express_selected;
        }

        @Override
        protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
            return new InnerContentViewHolder(rootView, viewType);
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

    public OnItemSelectListener getOnItemSelectListener() {
        return mOnItemSelectListener;
    }

    public PopupRecommendExpress setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        mOnItemSelectListener = onItemSelectListener;
        return this;
    }

    public interface OnItemSelectListener {
        void onItemSelected(ExpressCompanyInfo companyInfo);
    }

}
