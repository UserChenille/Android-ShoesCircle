package com.zjzf.shoescircle.ui.widget.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈志远 on 2017/04/20.
 * <p/>
 * banner指示器容器
 */
public class DotIndicatorContainer extends LinearLayout {
    private static final String TAG = "DotIndicator";
    private static final int DEFAULT_DOT_NUM = 1;
    private static final int MAX_DOT_NUM = 9;
    private static final int DOT_SIZE = 6;

    List<DotIndicator> mDotIndicators;

    private int currentSelection = 0;

    private int mDotsNum = DEFAULT_DOT_NUM;

    private int mDotNormalColor = -1;
    private int mDotSelectedColor = -1;

    private boolean attachBanner;
    private boolean hasInit;

    public DotIndicatorContainer(Context context) {
        this(context, null);
    }

    public DotIndicatorContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotIndicatorContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER);
    }

    public DotIndicatorContainer init(Context context, int dotNum) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        this.mDotsNum = dotNum == 0 ? DEFAULT_DOT_NUM : dotNum;
        buildDotView(context);
        setCurrentSelection(0);
        hasInit = true;
        return this;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (ToolUtil.isListEmpty(mDotIndicators)) {
            buildDotView(getContext());
            setCurrentSelection(currentSelection);
        }
    }

    /**
     * 初始化dotview
     */
    private void buildDotView(Context context) {
        if (getChildCount() > 0) removeAllViewsInLayout();
        mDotIndicators = new ArrayList<>();
        for (int i = 0; i < mDotsNum; i++) {
            DotIndicator dotIndicator = new DotIndicator(context);
            dotIndicator.setSelected(false);
            dotIndicator.setDotNormalColor(mDotNormalColor);
            dotIndicator.setDotSelectedColor(mDotSelectedColor);
            LayoutParams params = new LayoutParams(UIHelper.dip2px(DOT_SIZE), UIHelper.dip2px(DOT_SIZE));
            if (i == 0) {
                params.leftMargin = 0;
            } else {
                params.leftMargin = UIHelper.dip2px(6f);
            }
            addView(dotIndicator, params);
            mDotIndicators.add(dotIndicator);
        }
    }

    public int getCurrentSelection() {
        return currentSelection;
    }

    /**
     * 当前选中的dotview
     */
    public void setCurrentSelection(int selection) {
        this.currentSelection = selection;
        for (DotIndicator dotIndicator : mDotIndicators) {
            dotIndicator.setSelected(false);
        }
        if (selection >= 0 && selection < mDotIndicators.size()) {
            mDotIndicators.get(selection).setSelected(true);
        } else {
            Log.e(TAG, "the selection can not over dotViews size");
        }
    }

    public int getDotViewNum() {
        return mDotsNum;
    }

    /**
     * 当前需要展示的dotview数量
     */
    public void setDotViewNum(int num) {
        if (num > MAX_DOT_NUM || num <= 0) {
            LogHelper.trace(LogHelper.e, "num必须在1~" + MAX_DOT_NUM + "之间哦");
            return;
        }
        if (num <= 1) {
            removeAllViewsInLayout();
            setVisibility(GONE);
            return;
        }

        if (this.mDotsNum != num) {
            this.mDotsNum = num;
            mDotIndicators.clear();
            mDotIndicators = null;
            buildDotView(getContext());
            setCurrentSelection(currentSelection);
        }
/*
        for (DotIndicator dotIndicator : mDotIndicators) {
            dotIndicator.setVisibility(VISIBLE);
        }*/
        /*for (int i = num; i < mDotIndicators.size(); i++) {
            DotIndicator dotIndicator = mDotIndicators.get(i);
            if (dotIndicator != null) {
                dotIndicator.setSelected(false);
                dotIndicator.setVisibility(GONE);
            }
        }*/

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDotIndicators != null) {
            mDotIndicators.clear();
            LogHelper.trace( "清除dotindicator引用");
        }
    }

    public int getDotNormalColor() {
        return mDotNormalColor;
    }

    public void setDotNormalColor(int dotNormalColor) {
        mDotNormalColor = dotNormalColor;
        if (!ToolUtil.isListEmpty(mDotIndicators)) {
            for (DotIndicator dotIndicator : mDotIndicators) {
                dotIndicator.setDotNormalColor(mDotNormalColor);
            }
        }
    }

    public int getDotSelectedColor() {
        return mDotSelectedColor;
    }

    public void setDotSelectedColor(int dotSelectedColor) {
        mDotSelectedColor = dotSelectedColor;
        if (!ToolUtil.isListEmpty(mDotIndicators)) {
            for (DotIndicator dotIndicator : mDotIndicators) {
                dotIndicator.setDotSelectedColor(mDotSelectedColor);
            }
        }
    }

    public void bindViewPager(final ViewPager pager) {
        if (attachBanner) return;
        if (pager != null) {
            int count = 0;
            PagerAdapter adapter = pager.getAdapter();
            count = adapter == null ? 0 : adapter.getCount();
            if (adapter instanceof Banner.LoopPagerAdapter) {
                count = ((Banner.LoopPagerAdapter) adapter).getRealCount();
            }
            if (getDotViewNum() != count) {
                if (!hasInit) {
                    init(getContext(), count);
                } else {
                    setDotViewNum(count);
                }
            }
            final int finalCount = count;
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i2) {
                }

                @Override
                public void onPageSelected(int i) {
                    setCurrentSelection(finalCount > 0 ? i % finalCount : 0);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
            attachBanner = true;
        }
    }


}
