package com.zjzf.shoescircle.ui.widget.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zjzf.shoescircle.lib.base.interfaces.ClearMemoryObject;
import com.zjzf.shoescircle.lib.other.SimpleObjectPool;
import com.zjzf.shoescircle.lib.other.WeakHandler;
import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.uilib.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 陈志远 on 2017/4/20.
 * <p>
 * 通用性banner
 */

public class Banner<T> extends FrameLayout implements OnPageChangeListener, ClearMemoryObject {
    private static final String TAG = "PYBanner";
    public static final int VIEW_TAG_KEY = R.id.banner_imageview_tag_key;
    public static final int[] VIEW_TAGS = {R.id.banner_view_tag_1, R.id.banner_view_tag_2, R.id.banner_view_tag_3,
            R.id.banner_view_tag_4, R.id.banner_view_tag_5};
    private FixWrapViewpager bannerPager;
    private DotIndicatorContainer dotIndicatorContainer;
    private BannerPagerAdapterProxy mAdapter;
    private View bottomDivider;

    private List<T> datas;
    private InnerViewPool VIEW_POOL;

    private float ratio;

    private boolean isAutoPlay = true;
    private long playDelayTime = 5 * 1000;
    //是否无限循环
    private boolean isEndless = true;

    private boolean hasStartAutoPlay;
    private OnBannerPagerClick onBannerPagerClick;
    private WeakHandler InnerHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (bannerPager != null) {
                bannerPager.setCurrentItem(bannerPager.getCurrentItem() + 1);
                if (isAutoPlay) InnerHandler.sendEmptyMessageDelayed(0, playDelayTime);
            }
            return false;
        }
    });

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.isAutoPlay = true;
        this.isEndless = true;
        init(context);
    }

    public Banner(Context context, boolean isAutoPlay, boolean isEndless) {
        super(context);
        this.isAutoPlay = isAutoPlay;
        this.isEndless = isEndless;
        init(context);
    }

    /**
     * @param context
     * @param isAutoPlay
     * @param isEndless
     * @param ratio      宽高比
     */
    public Banner(Context context, boolean isAutoPlay, boolean isEndless, float ratio) {
        super(context);
        this.datas = new ArrayList<>();
        this.isAutoPlay = isAutoPlay;
        this.isEndless = isEndless;
        this.ratio = ratio;
        init(context);
    }

    private void init(Context context) {
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_banner, this);
        VIEW_POOL = new InnerViewPool(9);
        bannerPager = (FixWrapViewpager) findViewById(R.id.banner_pager);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bannerPager.getLayoutParams();
        float bannerWidth = UIHelper.getScreenWidth();
        float bannerHeight;
        if (ratio != 0) {
            bannerHeight = bannerWidth / ratio;
            params.height = (int) bannerHeight;
            bannerPager.setLayoutParams(params);
        }
        bannerPager.setOnPageChangeListener(this);
        ViewUtil.controlViewPagerScrollSpeed(bannerPager, new DecelerateInterpolator(), 500);
        dotIndicatorContainer = (DotIndicatorContainer) findViewById(R.id.banner_indicator);
        dotIndicatorContainer.init(context, datas == null ? 0 : datas.size());
    }

    private void setRatio(float ratio) {
        if (this.ratio == ratio) return;
        this.ratio = ratio;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bannerPager.getLayoutParams();
        float bannerWidth = UIHelper.getScreenWidth();
        float bannerHeight;
        if (ratio == 0) {
            ratio = 2f / 1f;
        }
        bannerHeight = bannerWidth / ratio;
        params.height = (int) bannerHeight;
        bannerPager.setLayoutParams(params);
    }

    public void setAdapter(SimpleBannerPagerAdapter<T> adapter) {
        if (this.mAdapter == null) {
            this.mAdapter = new BannerPagerAdapterProxy(adapter);
            bannerPager.setAdapter(mAdapter);
            if (!ToolUtil.isListEmpty(datas)) {
                dotIndicatorContainer.setDotViewNum(datas.size());
            }
        } else {
            mAdapter.setAdapter(adapter);
            notifyDataSetChanged();
        }
        if (isAutoPlay) startAutoPlay();
    }

    public void updateDatas(List<T> newDatas) {
        if (!ToolUtil.isListEmpty(newDatas)) {
            if (datas != null) {
                datas.clear();
                datas.addAll(newDatas);
            } else {
                datas = newDatas;
            }
            dotIndicatorContainer.setDotViewNum(datas.size());
            this.setVisibility(ToolUtil.isListEmpty(datas) ? GONE : VISIBLE);
            notifyDataSetChanged();
        }
    }

    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setBottomDividerVisible(int visible) {
        if (bottomDivider != null) {
            bottomDivider.setVisibility(visible);
        }
    }

    public void setDotIndicatorContainerVisible(int visible) {
        if (dotIndicatorContainer != null) {
            dotIndicatorContainer.setVisibility(visible);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (isAutoPlay) {
                // 开始图片滚动
                startAutoPlay();
            }
        } else {
            if (isAutoPlay) {
                // 停止图片滚动
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void startAutoPlay() {
        if (datas != null && datas.size() <= 1) return;
        if (isAutoPlay && InnerHandler != null && !hasStartAutoPlay) {
            InnerHandler.sendEmptyMessageDelayed(0, playDelayTime);
            hasStartAutoPlay = true;
        }
    }

    public void stopAutoPlay() {
        if (InnerHandler != null) {
            InnerHandler.removeCallbacksAndMessages(null);
            hasStartAutoPlay = false;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            stopAutoPlay();
            VIEW_POOL.clearPool();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isAutoPlay) {
            startAutoPlay();
        }
    }

    @Override
    public void clearMemory(boolean needSetNull) {
        try {
            if (!needSetNull) {
                clearBannerPagerBitmap(bannerPager, bannerPager.getCurrentItem());
            } else {
                clearBannerPagerBitmap(bannerPager, -1);
            }
            stopAutoPlay();
            if (VIEW_POOL != null) VIEW_POOL.clearPool();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OnBannerPagerClick getOnBannerPagerClick() {
        return onBannerPagerClick;
    }

    public void setOnBannerPagerClick(OnBannerPagerClick onBannerPagerClick) {
        this.onBannerPagerClick = onBannerPagerClick;
    }

    public ViewPager getBannerPager() {
        return bannerPager;
    }

    /**
     * =============================================================
     * viewpager interface
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (datas != null && datas.size() > 0)
            dotIndicatorContainer.setCurrentSelection(position % datas.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OnBannerPagerClick<T> {
        void onClick(T promotion);
    }

    //-----------------------------------------tool-----------------------------------------

    void clearBannerPagerBitmap(ViewPager viewPager, int notClear) {
        try {
            if (viewPager == null) return;
            for (int i = 0; i < Banner.VIEW_TAGS.length; i++) {
                View v = viewPager.findViewWithTag(Banner.VIEW_TAGS[i]);
                if (v != null && i != notClear % Banner.VIEW_TAGS.length) {
                    if (v instanceof ImageView) {
                        UIHelper.clearImageViewMemory((ImageView) v);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private static class InnerViewPool extends SimpleObjectPool<View> {

        public InnerViewPool(int size) {
            super(View.class, size);
        }

        @Override
        public void clearPool() {
            try {
                for (int i = 0; i < objsPool.length; i++) {
                    View v = objsPool[i];
                    if (v instanceof ImageView) {
                        UIHelper.clearImageViewMemory((ImageView) v);
                        objsPool[i] = null;
                    }
                }
                curPointer = -1;
            } catch (Exception e) {
                if (objsPool != null && objsPool.length > 0) {
                    for (View view : objsPool) {
                        if (view instanceof ImageView) {
                            UIHelper.clearImageViewMemory((ImageView) view);
                        }
                    }
                }
                curPointer = -1;
            }
        }
    }


    public void setDotNormalColor(int dotNormalColor) {
        if (dotIndicatorContainer != null) {
            dotIndicatorContainer.setDotNormalColor(dotNormalColor);
        }
    }


    public void setDotSelectedColor(int dotSelectedColor) {
        if (dotIndicatorContainer != null) {
            dotIndicatorContainer.setDotSelectedColor(dotSelectedColor);
        }
    }


    public void setDotIndicatorContainerMargin(int left, int top, int right, int bottom) {
        if (dotIndicatorContainer != null && dotIndicatorContainer.getLayoutParams() != null) {
            RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) dotIndicatorContainer.getLayoutParams();
            p.leftMargin = left;
            p.topMargin = top;
            p.rightMargin = right;
            p.bottomMargin = bottom;
            dotIndicatorContainer.setLayoutParams(p);
        }
    }

    private class BannerPagerAdapterProxy extends PagerAdapter implements LoopPagerAdapter {

        private SimpleBannerPagerAdapter<T> mBannerPagerAdapter;

        public BannerPagerAdapterProxy(SimpleBannerPagerAdapter<T> bannerPagerAdapter) {
            mBannerPagerAdapter = bannerPagerAdapter;
            datas = bannerPagerAdapter.onInitDatum();
        }

        void setAdapter(SimpleBannerPagerAdapter<T> bannerPagerAdapter) {
            this.mBannerPagerAdapter = bannerPagerAdapter;
        }

        @Override
        public int getCount() {
            if (mBannerPagerAdapter == null) return 0;
            return isEndless && !(datas.size() == 1) ? Integer.MAX_VALUE : datas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public int getItemPosition(Object object) {
            try {
                if (object != null) {
                    if (object instanceof ImageView) {
                        if ((int) ((ImageView) object).getTag() == VIEW_TAGS[bannerPager.getCurrentItem() % VIEW_TAGS.length]) {
                            return POSITION_UNCHANGED;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (mBannerPagerAdapter == null) return null;
            View v = VIEW_POOL.get();
            v = mBannerPagerAdapter.onInitView(getContext(), container, v, position % datas.size());
            if (v.getTag(VIEW_TAG_KEY) != null) v.setTag(VIEW_TAG_KEY, null);
            v.setTag(VIEW_TAG_KEY, VIEW_TAGS[position % VIEW_TAGS.length]);
            container.addView(v, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object instanceof ImageView) {
                UIHelper.clearImageViewMemory((ImageView) object);
            }
            container.removeView((View) object);
            ((View) object).setTag(VIEW_TAG_KEY, null);
            VIEW_POOL.put((View) object);
        }

        @Override
        public int getRealCount() {
            return datas == null ? 0 : datas.size();
        }
    }

    public static abstract class SimpleBannerPagerAdapter<T> {

        public abstract List<T> onInitDatum();

        public abstract View onInitView(Context context, ViewGroup container, View convertView, int position);

    }

    public interface LoopPagerAdapter {
        int getRealCount();
    }


}
