package com.zjzf.shoescircleandroid.widget.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.base.baseadapter.BaseRecyclerViewAdapter;
import com.zjzf.shoescircle.lib.base.baseadapter.BaseRecyclerViewHolder;
import com.zjzf.shoescircle.lib.base.baseadapter.OnBaseRecyclerViewItemClickListener;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.widget.GridSpacingItemDecoration;
import com.zjzf.shoescircleandroid.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 陈志远 on 2018/10/18.
 */
public class PopupSelectSize extends BasePopupWindow {

    private ImageView ivClose;
    private RecyclerView rvSmallSizeContent;
    private RecyclerView rvNormalSizeContent;
    private Button btnConfirm;

    private OnConfirmListener mConfirmListener;

    private static List<String> s_SmallSizes = new ArrayList<>();
    private static List<String> s_NormalSizes = new ArrayList<>();

    static {
        s_SmallSizes.add("35.5");
        s_SmallSizes.add("36");
        s_SmallSizes.add("36.5");
        s_SmallSizes.add("37");
        s_SmallSizes.add("37.5");
        s_SmallSizes.add("38");
        s_SmallSizes.add("38.5");
        s_SmallSizes.add("39");

        s_NormalSizes.add("40");
        s_NormalSizes.add("40.5");
        s_NormalSizes.add("41");
        s_NormalSizes.add("41.5");
        s_NormalSizes.add("42");
        s_NormalSizes.add("42.5");
        s_NormalSizes.add("43");
        s_NormalSizes.add("43.5");
        s_NormalSizes.add("44");
        s_NormalSizes.add("44.5");
        s_NormalSizes.add("45");
        s_NormalSizes.add("45.5");
        s_NormalSizes.add("46");
        s_NormalSizes.add("46.5");
        s_NormalSizes.add("47");
    }

    private List<String> selectedData = new ArrayList<>();

    private InnerAdapter smallSizeAdapter;
    private InnerAdapter normalSizeAdapter;

    public PopupSelectSize(Context context) {
        super(context);
        this.ivClose = findViewById(R.id.iv_close);
        this.rvSmallSizeContent = findViewById(R.id.rv_small_size_content);
        this.rvNormalSizeContent = findViewById(R.id.rv_normal_size_content);
        this.btnConfirm = findViewById(R.id.btn_confirm);
        setBlurBackgroundEnable(true);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        initView();
    }

    private void initView() {
        smallSizeAdapter = new InnerAdapter(getContext(), s_SmallSizes);
        normalSizeAdapter = new InnerAdapter(getContext(), s_NormalSizes);

        smallSizeAdapter.setOnRecyclerViewItemClickListener(new OnBaseRecyclerViewItemClickListener<String, InnerAdapter.InnerViewHolder>() {
            @Override
            public void onItemClick(InnerAdapter.InnerViewHolder holder, View v, int position, String data) {
                super.onItemClick(holder, v, position, data);
                updateTextState(smallSizeAdapter, position, holder);
            }

            @Override
            public void onItemClick(View v, int position, String data) {

            }
        });
        normalSizeAdapter.setOnRecyclerViewItemClickListener(new OnBaseRecyclerViewItemClickListener<String, InnerAdapter.InnerViewHolder>() {
            @Override
            public void onItemClick(InnerAdapter.InnerViewHolder holder, View v, int position, String data) {
                super.onItemClick(holder, v, position, data);
                updateTextState(normalSizeAdapter, position, holder);
            }

            @Override
            public void onItemClick(View v, int position, String data) {

            }
        });

        rvSmallSizeContent.setLayoutManager(new GridLayoutManager(getContext(), 6));
        rvNormalSizeContent.setLayoutManager(new GridLayoutManager(getContext(), 6));

        rvSmallSizeContent.setItemAnimator(null);
        rvNormalSizeContent.setItemAnimator(null);

        rvSmallSizeContent.addItemDecoration(new GridSpacingItemDecoration(6, UIHelper.dip2px(3), false));
        rvNormalSizeContent.addItemDecoration(new GridSpacingItemDecoration(6, UIHelper.dip2px(3), false));

        rvSmallSizeContent.setAdapter(smallSizeAdapter);
        rvNormalSizeContent.setAdapter(normalSizeAdapter);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConfirmListener != null) {

                    List<String> result = new ArrayList<>(ToolUtil.removeDuplicate(selectedData));
                    Collections.sort(result);
                    mConfirmListener.onConfirmListener(result);
                }
                dismiss();
            }
        });

    }

    private void updateTextState(InnerAdapter adapter, int position, InnerAdapter.InnerViewHolder holder) {
        if (holder != null && holder.tvSize != null) {
            String size = holder.tvSize.getText().toString().trim();
            boolean selected = holder.tvSize.isSelected();
            holder.tvSize.setSelected(!selected);
            if (holder.tvSize.isSelected()) {
                if (!selectedData.contains(size)) {
                    selectedData.add(size);
                }
            } else {
                selectedData.remove(size);
            }
            adapter.notifyItemChanged(position);
        }
    }

    public void clearSelected() {
        selectedData.clear();
    }

    public void setSelectedData(String selectedDataStr) {
        if (StringUtil.noEmpty(selectedDataStr)) {
            selectedData.add(selectedDataStr);
        }
    }

    public void setSelectedData(List<String> selectedDataList) {
        if (selectedDataList != null) {
            selectedData.addAll(selectedDataList);
        }
    }

    public void setSelectedData(String... selectedDatas) {
        if (selectedDatas != null) {
            selectedData.addAll(Arrays.asList(selectedDatas));
        }
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_select_sizes);
    }

    @Override
    public void showPopupWindow() {
        smallSizeAdapter.updateData(s_SmallSizes);
        normalSizeAdapter.updateData(s_NormalSizes);
        super.showPopupWindow();
    }

    public OnConfirmListener getConfirmListener() {
        return mConfirmListener;
    }

    public PopupSelectSize setOnConfirmListener(OnConfirmListener confirmListener) {
        mConfirmListener = confirmListener;
        return this;
    }

    public interface OnConfirmListener {
        void onConfirmListener(List<String> selectedSize);
    }

    private class InnerAdapter extends BaseRecyclerViewAdapter<String> {


        public InnerAdapter(@NonNull Context context, @NonNull List<String> datas) {
            super(context, datas);
        }

        @Override
        protected int getViewType(int position, @NonNull String data) {
            return 0;
        }

        @Override
        protected int getLayoutResId(int viewType) {
            return R.layout.item_tv_size;
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
                tvSize.setSelected(selectedData.contains(data));
                tvSize.setText(data);
            }
        }

    }
}
