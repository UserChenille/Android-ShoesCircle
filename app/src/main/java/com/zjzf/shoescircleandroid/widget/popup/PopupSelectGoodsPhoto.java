package com.zjzf.shoescircleandroid.widget.popup;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.base.baseadapter.BaseRecyclerViewAdapter;
import com.zjzf.shoescircle.lib.base.baseadapter.BaseRecyclerViewHolder;
import com.zjzf.shoescircle.lib.base.baseadapter.OnRecyclerViewItemClickListener;
import com.zjzf.shoescircle.lib.helper.RxHelper;
import com.zjzf.shoescircle.lib.manager.ImageLoaderManager;
import com.zjzf.shoescircle.lib.utils.MultiSpanUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.ui.widget.GridSpacingItemDecoration;
import com.zjzf.shoescircle.ui.widget.LoadingView;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.net.apis.GoodsApis;
import com.zjzf.shoescircleandroid.base.net.client.HandleServerApiErrorAction;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.model.GoodsImageInfo;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 陈志远 on 2018/8/29.
 */
public class PopupSelectGoodsPhoto extends BasePopupWindow {
    RecyclerView rvContent;
    TextView tvRefresh;
    LinearLayout popupContainer;
    private LoadingView mLoadingView;

    private InnerPhotoAdapter mInnerPhotoAdapter;
    private OnPhotoSelectListener mOnPhotoSelectListener;
    private String artNo;

    public PopupSelectGoodsPhoto(Context context) {
        super(context);
        this.rvContent = (RecyclerView) findViewById(R.id.rv_content);
        this.tvRefresh = (TextView) findViewById(R.id.tv_refresh);
        this.popupContainer = (LinearLayout) findViewById(R.id.popup_container);
        this.mLoadingView = (LoadingView) findViewById(R.id.loading_view);
        setPopupWindowFullScreen(true)
                .setBlurBackgroundEnable(true)
                .setBackPressEnable(false);

        MultiSpanUtil.create("没有合适的？刷新一下")
                .append("刷新一下")
                .setTextColor(Color.parseColor("#2E8AE6"))
                .setSpanClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reload();
                    }
                }).into(tvRefresh);

        mInnerPhotoAdapter = new InnerPhotoAdapter(getContext());
        rvContent.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvContent.addItemDecoration(new GridSpacingItemDecoration(3, UIHelper.dip2px(8), false));
        mInnerPhotoAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<GoodsImageInfo>() {
            @Override
            public void onItemClick(View v, int position, GoodsImageInfo data) {
                if (mOnPhotoSelectListener != null) {
                    mOnPhotoSelectListener.onSelect(data.getImageUrl());
                }
                dismiss();
            }
        });
        rvContent.setAdapter(mInnerPhotoAdapter);
    }

    public void showPopupWindow(String artNo) {
        this.artNo = artNo;
        reload();
        super.showPopupWindow();
    }

    private void reload() {
        mLoadingView.start();
        ViewUtil.setViewsVisible(View.VISIBLE, mLoadingView);
        ViewUtil.setViewsVisible(View.INVISIBLE, rvContent);

        RetrofitClient.get()
                .create(GoodsApis.class)
                .searchGoodsImages(artNo)
                .doOnNext(new HandleServerApiErrorAction<>())
                .compose(RxHelper.<ObjectData<List<GoodsImageInfo>>>io_main())
                .subscribe(new OnResponseListener<ObjectData<List<GoodsImageInfo>>>() {
                    @Override
                    public void onNext(ObjectData<List<GoodsImageInfo>> result) {
                        if (result.getData() != null) {
                            success(result.getData());
                        }
                    }
                });

    }

    private void success(List<GoodsImageInfo> data) {
        mInnerPhotoAdapter.updateData(data);
        ViewUtil.setViewsVisible(View.GONE, mLoadingView);
        ViewUtil.setViewsVisible(View.VISIBLE, rvContent);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultAlphaAnimation(true);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_goods_photo);
    }

    private class InnerPhotoAdapter extends BaseRecyclerViewAdapter<GoodsImageInfo> {

        public InnerPhotoAdapter(@NonNull Context context) {
            super(context);
        }

        @Override
        public int getItemCount() {
            return Math.min(9, super.getItemCount());
        }

        @Override
        protected int getViewType(int position, @NonNull GoodsImageInfo data) {
            return 0;
        }

        @Override
        protected int getLayoutResId(int viewType) {
            return R.layout.item_photo;
        }

        @Override
        protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
            return new InnerViewHolder(rootView, viewType);
        }
    }

    private class InnerViewHolder extends BaseRecyclerViewHolder<GoodsImageInfo> {
        ImageView mImageView;

        public InnerViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            mImageView = findViewById(R.id.iv_photo);
        }

        @Override
        public void onBindData(final GoodsImageInfo data, int position) {
            ImageLoaderManager.INSTANCE.loadImage(mImageView, data.getThumbnailUrl());
        }
    }

    public OnPhotoSelectListener getOnPhotoSelectListener() {
        return mOnPhotoSelectListener;
    }

    public PopupSelectGoodsPhoto setOnPhotoSelectListener(OnPhotoSelectListener onPhotoSelectListener) {
        mOnPhotoSelectListener = onPhotoSelectListener;
        return this;
    }

    public interface OnPhotoSelectListener {
        void onSelect(String url);
    }

}
