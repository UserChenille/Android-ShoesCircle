package com.zjzf.shoescircle.ui.widget.pullrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.ui.widget.EmptyView;
import com.zjzf.shoescircle.ui.widget.pullrecyclerview.config.Mode;
import com.zjzf.shoescircle.ui.widget.pullrecyclerview.config.PullMode;
import com.zjzf.shoescircle.ui.widget.pullrecyclerview.config.PullState;
import com.zjzf.shoescircle.ui.widget.pullrecyclerview.footer.PtrFooter;
import com.zjzf.shoescircle.ui.widget.pullrecyclerview.interfaces.OnRefreshListener;
import com.zjzf.shoescircle.ui.widget.pullrecyclerview.util.RecyclerViewUtil;
import com.zjzf.shoescircle.ui.widget.pullrecyclerview.wrapperadapter.FixedViewInfo;
import com.zjzf.shoescircle.ui.widget.pullrecyclerview.wrapperadapter.HeaderViewWrapperAdapter;
import com.zjzf.shoescircle.uilib.R;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.zjzf.shoescircle.ui.widget.pullrecyclerview.config.PullMode.FROM_BOTTOM;
import static com.zjzf.shoescircle.ui.widget.pullrecyclerview.config.PullMode.FROM_START;
import static com.zjzf.shoescircle.ui.widget.pullrecyclerview.config.PullState.NORMAL;
import static com.zjzf.shoescircle.ui.widget.pullrecyclerview.config.PullState.REFRESHING;
import static com.zjzf.shoescircle.ui.widget.pullrecyclerview.wrapperadapter.FixedViewInfo.ITEM_VIEW_TYPE_FOOTER_START;
import static com.zjzf.shoescircle.ui.widget.pullrecyclerview.wrapperadapter.FixedViewInfo.ITEM_VIEW_TYPE_HEADER_START;


/**
 * Created by 陈志远 on 2017/6/1.
 * <p>
 * 下拉刷新/上拉加载更多的recyclerview
 */

public class PullRecyclerView extends PtrFrameLayout implements PtrHandler {
    private static final String TAG = "PullRecyclerView";

    protected static final int DEFAULT_ROW = 10;
    protected static final int FIRST_PAGE = 1;

    //-----------------------------------------views-----------------------------------------
    private PtrClassicDefaultHeader ptrHeader;
    private FrameLayout content;
    private EmptyView mEmptyView;
    private FrameLayout ptrFooter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    //-----------------------------------------interfaces-----------------------------------------
    private OnRefreshListener mOnRefreshListener;
    private OnPreDispatchTouchListener onPreDispatchTouchListener;
    private OnCompeleteFinishListgener mOnCompeleteFinishListgener;

    //-----------------------------------------状态-----------------------------------------
    private PullMode pullMode;
    private PullState currentStatus = NORMAL;
    private Mode mode;

    //-----------------------------------------分页-----------------------------------------
    private int currentPage = FIRST_PAGE;
    private int requestRows = DEFAULT_ROW;
    private boolean isAuto;
    private boolean canLoadMore;
    private boolean canPull;

    //-----------------------------------------behavior-----------------------------------------
    private boolean checkBehavior;
    private int mVerticalOffset;

    public PullRecyclerView(Context context) {
        this(context, null);
    }

    public PullRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        Drawable background = null;
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.PullRecyclerView);
        background = a.getDrawable(R.styleable.PullRecyclerView_android_background);
        if (background != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(background);
            } else {
                setBackgroundDrawable(background);
            }
        } else {
            setBackgroundColor(Color.TRANSPARENT);
        }
        a.recycle();
        setCanPull(true);
        //暂时用经典header
        content = new FrameLayout(context);
        mEmptyView = new EmptyView(context);

        ptrHeader = new PtrClassicDefaultHeader(context);
        ptrFooter = new PtrFooter(context);
        ptrFooter.setVisibility(GONE);
        //rv
        mRecyclerView = new RecyclerView(context);
        mLinearLayoutManager = new LinearLayoutManager(context);
        //渲染优化，放到render thread做，（prefetch在v25之后可用），机型在萝莉炮(lollipop)后才可以享受此优化（事实上默认是开启的）
        mLinearLayoutManager.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        //取消默认item变更动画
        mRecyclerView.setItemAnimator(null);
        //add view
        content.addView(mEmptyView);
        content.addView(mRecyclerView);
        setHeaderView(ptrHeader);
        addView(content);
        addFooterView(ptrFooter);
        //ptr option
        addPtrUIHandler(ptrHeader);
        setPtrHandler(this);
        setResistance(2.3f);
//        setRatioOfHeaderHeightToRefresh(.25f);
//        setDurationToClose(200);
//        setDurationToCloseHeader(1000);
        //刷新时的固定的偏移量
        //setOffsetToKeepHeaderWhileLoading(0);

        //下拉刷新，即下拉到距离就刷新而不是松开刷新
        //setPullToRefresh(false);
        //刷新的时候保持头部？
        //setKeepHeaderWhenRefresh(false);
        setMode(Mode.BOTH);
        mEmptyView.setOnEmptyClickListener(new EmptyView.OnEmptyClickListener() {
            @Override
            public void onClick(View v) {
                manualRefresh();
            }
        });
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view1) {
        if (checkBehavior) {
            return mVerticalOffset == 0 && canRefresh() && RecyclerViewUtil.isOnTop(mRecyclerView) && canPull;
        } else {
            return canRefresh() && RecyclerViewUtil.isOnTop(mRecyclerView) && canPull;
        }
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        if (canRefresh()) {
            callRefresh();
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        setAdapter(adapter, mLinearLayoutManager);
    }

    public void setAdapter(RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager) {
        if (mRecyclerView == null) return;
        if (adapter != null) {
            RecyclerView.Adapter wrapAdapter = adapter;
            if (mHeaderViewInfos.size() > 0 || mFooterViewInfos.size() > 0) {
                wrapAdapter = wrapHeaderRecyclerViewAdapterInternal(adapter, mHeaderViewInfos, mFooterViewInfos);
            }
            try {
                wrapAdapter.registerAdapterDataObserver(mDataObserver);
            } catch (Exception e) {
                //捕捉注册观察者的exception，但不需要处理
            }
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(wrapAdapter);
            handleEmptyView();
        }
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        mRecyclerView.setNestedScrollingEnabled(enabled);
    }

    /**
     * 让ptr兼容appbarlayout
     *
     * @param view
     */
    public void setUpAppBarLayout(View view) {
        mVerticalOffset = 0;
        if (view == null || !(view instanceof AppBarLayout)) {
            checkBehavior = false;
        } else {
            checkBehavior = true;
            ((AppBarLayout) view).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    mVerticalOffset = verticalOffset;
                }
            });
        }
    }

    public RecyclerView.Adapter getAdapter() {
        return mRecyclerView.getAdapter();
    }

    public PtrClassicDefaultHeader getHeader() {
        return this.ptrHeader;
    }

    /**
     * 监听adapter的数据变化
     */
    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            handleEmptyView();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        }
    };

    public void handleEmptyView() {
        if (mRecyclerView != null) {
            RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
            if (adapter != null) {
                final int itemCount = adapter.getItemCount();
                if (adapter instanceof HeaderViewWrapperAdapter) {
                    int defaultViewCount = ((HeaderViewWrapperAdapter) adapter).getHeadersCount() +
                            ((HeaderViewWrapperAdapter) adapter).getFootersCount();
                    handleEmptyView(itemCount <= defaultViewCount);
                } else {
                    handleEmptyView(itemCount <= 0);
                }
            }
        }
    }

    public void handleEmptyView(boolean showEmptyView) {
        ViewUtil.setViewsVisible(showEmptyView ? GONE : VISIBLE, mRecyclerView);
        ViewUtil.setViewsVisible(showEmptyView ? VISIBLE : GONE, mEmptyView);
    }

    public void cancelEmptyViewClickEvent(boolean cancel) {
        if (cancel) {
            mEmptyView.setOnEmptyClickListener(null);
        } else {
            mEmptyView.setOnEmptyClickListener(new EmptyView.OnEmptyClickListener() {
                @Override
                public void onClick(View v) {
                    manualRefresh();
                }
            });
        }
    }

    public void setEmptyViewText(CharSequence text) {
        mEmptyView.setEmptyText(text);
    }

    public void setLastUpdateTimeKey(String key) {
        if (ptrHeader != null) {
            ptrHeader.setLastUpdateTimeKey(key);
        }
    }

    public void setLastUpdateTimeRelateObject(Object object) {
        if (ptrHeader != null) {
            ptrHeader.setLastUpdateTimeRelateObject(object);
        }
    }

    public void compelete() {
        hasDrag = false;
        isAuto = false;
        if (pullMode == FROM_BOTTOM) {
            onLoadMoreFinish();
        }
        if (pullMode == FROM_START) {
            onRefreshFinish();
        }
        currentPage++;
        setCurrentStatus(NORMAL);
        refreshComplete();
        if (mOnCompeleteFinishListgener != null) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOnCompeleteFinishListgener.onCompeleteFinish();
                }
            }, getDurationToCloseHeader() + 300);
        }
    }


    public void manualRefresh() {
        if (!canRefresh()) return;
        isAuto = true;
        //必须延迟，否则因为过快，还没measure完，也就是headerHeight=0就导致了自动刷新刷在了0位置
        postDelayed(new Runnable() {
            @Override
            public void run() {
                autoRefresh(true);
            }
        }, 200);
    }

    /**
     * scroll listener
     * <p>
     * 空闲状态： SCROLL_STATE_IDLE 0
     * 滑动状态： SCROLL_STATE_DRAGGING 1
     * 惯性滑动状态： SCROLL_STATE_FLING 2
     */
    private boolean hasDrag;//这个标志为是为了防止加载数量不足一屏导致回调了loadmore的情况，当且仅当有过手动滑动才会去加载更多
    private RecyclerView.OnScrollListener mInnerScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == 1) hasDrag = true;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (RecyclerViewUtil.isScrollToBottom(mRecyclerView) && canLoadMore() && !isAuto) {
                callLoadMore();
            }
        }
    };


    void callRefresh() {
        if (mOnRefreshListener != null) {
            handleEmptyView(false);
            setLastUpdateTimeRelateObject(this);
            setCurrentMode(FROM_START);
            setCurrentStatus(REFRESHING);
            onRefresh();
            currentPage = FIRST_PAGE;
            mOnRefreshListener.onRefreshing(this, mRecyclerView, currentPage, requestRows);
            LogHelper.trace(TAG, ">>>refresh<<<   page=[" + currentPage + "]");
        }
    }

    void callLoadMore() {
        if (mOnRefreshListener != null) {
            setCurrentMode(FROM_BOTTOM);
            setCurrentStatus(REFRESHING);
            onLoadMore();
            mOnRefreshListener.onLoadMore(this, mRecyclerView, currentPage, requestRows);
            LogHelper.trace(TAG, ">>>loadmore<<<   page=[" + currentPage + "]");
        }
    }

    void onLoadMore() {
        ViewUtil.setViewsVisible(VISIBLE, ptrFooter);
    }

    void onRefresh() {
    }

    void onLoadMoreFinish() {
        ViewUtil.setViewsVisible(GONE, ptrFooter);
    }

    void onRefreshFinish() {
    }

    boolean canRefresh() {
        return currentStatus != REFRESHING && (mode == Mode.REFRESH || mode == Mode.BOTH) && canPull;
    }

    boolean canLoadMore() {
        return hasDrag && currentStatus != REFRESHING && (mode == Mode.LOADMORE || mode == Mode.BOTH) && canLoadMore;
    }

    //-----------------------------------------header/footer-----------------------------------------
    //------------------------------------------分割线-----------------------------------------------

    /**
     * 以下为recyclerview 的headeradapter实现方案
     * <p>
     * 以Listview的headerView和footerView为模板做出的recyclerview的header和footer
     */
    private ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList<>();
    private ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList<>();

    public void addHeaderView(View headerView) {
        final FixedViewInfo info = new FixedViewInfo(headerView, ITEM_VIEW_TYPE_HEADER_START - mHeaderViewInfos.size());
        if (mHeaderViewInfos.size() == Math.abs(ITEM_VIEW_TYPE_FOOTER_START - ITEM_VIEW_TYPE_HEADER_START)) {
            mHeaderViewInfos.remove(mHeaderViewInfos.size() - 1);
        }
        if (checkFixedViewInfoNotAdded(info, mHeaderViewInfos)) {
            mHeaderViewInfos.add(info);
        }
        checkAndNotifyWrappedViewAdd(mRecyclerView.getAdapter(), info, true);
    }

    private void checkAndNotifyWrappedViewAdd(RecyclerView.Adapter adapter, FixedViewInfo info, boolean isHeader) {
        //header和footer只能再setAdapter前使用，如果是set了之后再用，为何不add普通的viewholder而非要Headr或者footer呢
        if (adapter != null) {
            if (!(adapter instanceof HeaderViewWrapperAdapter)) {
                adapter = wrapHeaderRecyclerViewAdapterInternal(adapter);
                if (isHeader) {
                    adapter.notifyItemInserted(((HeaderViewWrapperAdapter) adapter).findHeaderPosition(info.view));
                } else {
                    adapter.notifyItemInserted(((HeaderViewWrapperAdapter) adapter).findFooterPosition(info.view));
                }
            }
        }
    }

    public void addFooterView(View footerView) {
        final FixedViewInfo info = new FixedViewInfo(footerView, ITEM_VIEW_TYPE_FOOTER_START - mFooterViewInfos.size());
        if (checkFixedViewInfoNotAdded(info, mFooterViewInfos)) {
            mFooterViewInfos.add(info);
        }
        checkAndNotifyWrappedViewAdd(mRecyclerView.getAdapter(), info, false);
    }

    private boolean checkFixedViewInfoNotAdded(FixedViewInfo info, List<FixedViewInfo> infoList) {
        boolean result = true;
        if (ToolUtil.isListEmpty(infoList) || info == null) {
            result = true;
        } else {
            for (FixedViewInfo fixedViewInfo : infoList) {
                if (fixedViewInfo.view == info.view) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public int getHeaderViewCount() {
        return mHeaderViewInfos.size();
    }

    public int getFooterViewCount() {
        return mFooterViewInfos.size();
    }

    protected HeaderViewWrapperAdapter wrapHeaderRecyclerViewAdapterInternal(@NonNull RecyclerView.Adapter mWrappedAdapter,
                                                                             ArrayList<FixedViewInfo> mHeaderViewInfos,
                                                                             ArrayList<FixedViewInfo> mFooterViewInfos) {
        return new HeaderViewWrapperAdapter(mRecyclerView, mWrappedAdapter, mHeaderViewInfos, mFooterViewInfos);

    }

    protected HeaderViewWrapperAdapter wrapHeaderRecyclerViewAdapterInternal(@NonNull RecyclerView.Adapter mWrappedAdapter) {
        return wrapHeaderRecyclerViewAdapterInternal(mWrappedAdapter, mHeaderViewInfos, mFooterViewInfos);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        boolean hasConsume = false;
        if (onPreDispatchTouchListener != null)
            hasConsume = onPreDispatchTouchListener.OnDispatchTouchEvent(e);
        return hasConsume || super.dispatchTouchEvent(e);
    }

    //-----------------------------------------getter/setter-----------------------------------------
    public OnPreDispatchTouchListener getOnPreDispatchTouchListener() {
        return onPreDispatchTouchListener;
    }

    public void setOnPreDispatchTouchListener(OnPreDispatchTouchListener onPreDispatchTouchListener) {
        this.onPreDispatchTouchListener = onPreDispatchTouchListener;
    }

    void setCurrentStatus(PullState status) {
        this.currentStatus = status;
    }

    void setCurrentMode(PullMode mode) {
        this.pullMode = mode;
    }

    public void setLoadMoreEnable(List<?> data) {
        if (data == null || data.size() <= 0) {
            setLoadMoreEnable(false);
        } else {
            setLoadMoreEnable(data.size() >= requestRows);
        }
    }

    public void setLoadMoreEnable(boolean enable) {
        if (mRecyclerView == null) return;
        canLoadMore = enable;
        if (enable) {
            mRecyclerView.addOnScrollListener(mInnerScrollListener);
        } else {
            mRecyclerView.removeOnScrollListener(mInnerScrollListener);
        }
    }

    public void setCanPull(boolean canPull) {
        this.canPull = canPull;
    }

    public boolean isCanPull() {
        return this.canPull;
    }

    public boolean isCanLoadMore() {
        return this.canLoadMore;
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(decor);
        }
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        switch (mode) {
            case REFRESH:
                setCanPull(true);
                setLoadMoreEnable(false);
                break;
            case BOTH:
                setCanPull(true);
                setLoadMoreEnable(true);
                break;
            case LOADMORE:
                setCanPull(false);
                setLoadMoreEnable(true);
                break;
            case NONE:
                setLoadMoreEnable(false);
                setCanPull(false);
                break;
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getRequestRows() {
        return requestRows;
    }

    public void setRequestRows(int requestRows) {
        this.requestRows = requestRows;
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        if (mRecyclerView == null) return;
        mRecyclerView.addOnScrollListener(listener);
    }

    public PullMode getPullMode() {
        return pullMode;
    }


    public void removeOnScrollListener(RecyclerView.OnScrollListener listener) {
        if (mRecyclerView == null) return;
        mRecyclerView.removeOnScrollListener(listener);
    }

    public OnRefreshListener getOnRefreshListener() {
        return mOnRefreshListener;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public OnCompeleteFinishListgener getOnCompeleteFinishListgener() {
        return mOnCompeleteFinishListgener;
    }

    public void setOnCompeleteFinishListgener(OnCompeleteFinishListgener onCompeleteFinishListgener) {
        mOnCompeleteFinishListgener = onCompeleteFinishListgener;
    }

    /**
     * ============================================================= InterFace
     */
    public interface OnPreDispatchTouchListener {
        /**
         * @param ev
         * @return ture意味着已经消费事件，则不会传递下去
         */
        boolean OnDispatchTouchEvent(MotionEvent ev);
    }

    public interface OnCompeleteFinishListgener {
        void onCompeleteFinish();
    }
}
