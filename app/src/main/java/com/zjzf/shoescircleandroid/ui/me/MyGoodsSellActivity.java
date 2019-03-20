package com.zjzf.shoescircleandroid.ui.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.View;

import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.widget.viewpager.PagerSlidingIndicator;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.model.BaseActivityOption;
import com.zjzf.shoescircleandroid.ui.me.fragments.BaseMyGoodsFragment;
import com.zjzf.shoescircleandroid.ui.me.fragments.goods.sell.MyGoodsSellCompleteFragment;
import com.zjzf.shoescircleandroid.ui.me.fragments.goods.sell.MyGoodsSellHasSendFragment;
import com.zjzf.shoescircleandroid.ui.me.fragments.goods.sell.MyGoodsSellWaitGatherFragment;
import com.zjzf.shoescircleandroid.ui.me.fragments.goods.sell.MyGoodsSellWaitSendFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 陈志远 on 2018/11/24.
 * <p>
 * 我的出售
 */
public class MyGoodsSellActivity extends BaseActivity {
    @BindView(R.id.pager_indicator)
    PagerSlidingIndicator mPagerIndicator;
    @BindView(R.id.vp_container)
    ViewPager mVpContainer;

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_goods_sell;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onInitView(View decorView) {
        mPagerIndicator.setIndicatorPaddingLeftAndRight(UIHelper.dip2px(32));
        mPagerIndicator.setIndicatorHeight(UIHelper.dip2px(2));
        mPagerIndicator.setUnderlineColor(Color.TRANSPARENT);
        mPagerIndicator.setIndicatorBottomPadding(UIHelper.dip2px(3));
        mPagerIndicator.setTextColor(UIHelper.getColor(R.color.color_black2));
        mPagerIndicator.setShouldExpand(true);
        mPagerIndicator.setSelectedTextColor(UIHelper.getColor(R.color.colorPrimary));
        mPagerIndicator.setTextSize(UIHelper.sp2px(14));
        mPagerIndicator.setDividerColor(Color.TRANSPARENT);

        mVpContainer.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mPagerIndicator.setViewPager(mVpContainer);
    }


    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Pair<String, ? extends BaseMyGoodsFragment>> mFragments;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<>();
            mFragments.add(Pair.create("待发货", new MyGoodsSellWaitSendFragment()));
            mFragments.add(Pair.create("待收款", new MyGoodsSellWaitGatherFragment()));
            mFragments.add(Pair.create("已发货", new MyGoodsSellHasSendFragment()));
            mFragments.add(Pair.create("已完成", new MyGoodsSellCompleteFragment()));
        }

        @Override
        public BaseMyGoodsFragment getItem(int position) {
            return mFragments.get(position).second;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return getItem(position).getTitle();
        }
    }

    public static class Option extends BaseActivityOption<Option> {

    }

}
