package com.zjzf.shoescircleandroid.widget.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.base.baseadapter.BaseRecyclerViewAdapter;
import com.zjzf.shoescircle.lib.base.baseadapter.BaseRecyclerViewHolder;
import com.zjzf.shoescircle.lib.base.baseadapter.OnRecyclerViewItemClickListener;
import com.zjzf.shoescircle.lib.utils.MultiSpanUtil;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.api.IObjectTag;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 陈志远 on 2018/10/17.
 */
public class PopupAutoInput<T extends IObjectTag<String>> extends BasePopupWindow {
    private RecyclerView rvContent;
    private String key;
    private int type;
    private InnerAdapter mAdapter;
    private OnItemClickListener mOnItemClickListener;

    public PopupAutoInput(Context context) {
        super(context);
        rvContent = findViewById(R.id.rv_content);
        setAlignBackground(false);
        setOffsetY((int) dipToPx(8));
        setAllowDismissWhenTouchOutside(false);
        setAllowInterceptTouchEvent(false);
        setBackgroundColor(Color.TRANSPARENT);
        findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setPopupGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        getPopupWindow().setFocusable(false);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(-1f, 0f, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0f, -1f, 300);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_auto_input);
    }

    public void showPopupWindow(View anchorView, List<T> data, String key) {
        showPopupWindow(anchorView, 0, data, key);
    }

    public void showPopupWindow(View anchorView, int type, List<T> data, String key) {
        if (ToolUtil.isListEmpty(data)) return;
        this.key = key;
        this.type = type;
        if (mAdapter == null) {
            mAdapter = new InnerAdapter(getContext());
            mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<T>() {
                @Override
                public void onItemClick(View v, int position, T data) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClicked(PopupAutoInput.this.type, data);
                        dismiss();
                    }
                }
            });
            rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
            rvContent.setAdapter(mAdapter);
        }
        mAdapter.updateData(data);

        super.showPopupWindow(anchorView);
    }

    public void updateData(View anchorView, int type, List<T> data, String key) {
        if (ToolUtil.isListEmpty(data)) return;
        this.key = key;
        this.type = type;
        if (mAdapter == null) {
            mAdapter = new InnerAdapter(getContext());
            mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<T>() {
                @Override
                public void onItemClick(View v, int position, T data) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClicked(PopupAutoInput.this.type, data);
                        dismiss();
                    }
                }
            });
            rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
            rvContent.setAdapter(mAdapter);
        }
        mAdapter.updateData(data);
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public PopupAutoInput setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }

    public interface OnItemClickListener<T extends IObjectTag<String>> {
        void onItemClicked(int type, T data);
    }

    private class InnerAdapter extends BaseRecyclerViewAdapter<T> {

        public InnerAdapter(@NonNull Context context) {
            super(context);
        }

        @Override
        protected int getViewType(int position, @NonNull T data) {
            return 0;
        }

        @Override
        protected int getLayoutResId(int viewType) {
            return R.layout.item_popup_auto_input;
        }

        @Override
        protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
            return new InnerViewHolder(rootView, viewType);
        }

        class InnerViewHolder extends BaseRecyclerViewHolder<T> {
            TextView content;
            private int highLightColor = UIHelper.getColor(R.color.common_red);

            public InnerViewHolder(View itemView, int viewType) {
                super(itemView, viewType);
                content = findViewById(R.id.tv_content);
            }

            @Override
            public void onBindData(T data, int position) {
                MultiSpanUtil.create(data.get())
                        .append(key).setTextColor(highLightColor).setTextType(Typeface.DEFAULT_BOLD)
                        .into(content);
            }
        }
    }
}
