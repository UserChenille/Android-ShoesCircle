package com.zjzf.shoescircle.ui.utils;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ReplacementSpan;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircle.lib.utils.RandomUtil;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.widget.FixedSpeedScroller;

import java.lang.reflect.Field;

/**
 * Created by 陈志远 on 2017/4/28.
 * <p>
 * view相关工具类
 */

public class ViewUtil {
    /**
     * 更换viewpager的滑动scroll速度
     *
     * @param vp
     * @param duration
     */
    public static void controlViewPagerScrollSpeed(ViewPager vp, int duration) {
        controlViewPagerScrollSpeed(vp, new AccelerateInterpolator(), duration);
    }

    /**
     * 更换viewpager的scroll
     *
     * @param vp
     * @param interpolator
     * @param duration
     */
    public static void controlViewPagerScrollSpeed(ViewPager vp, Interpolator interpolator, int duration) {
        if (vp == null || duration == 0) return;
        try {
            if (interpolator == null) interpolator = new DecelerateInterpolator();
            Interpolator sInterpolator = interpolator;
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(vp.getContext(), sInterpolator, duration);
            scroller.setmDuration(duration);
            mScroller.set(vp, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static void setViewsClickListener(@Nullable View.OnClickListener listener, View... views) {
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(listener);
            }
        }
    }

    public static void setViewsClickListener(@NonNull View rootView, @Nullable View.OnClickListener listener, @IdRes int... ids) {
        if (rootView == null) return;
        for (int id : ids) {
            View v = rootView.findViewById(id);
            if (v != null) {
                v.setOnClickListener(listener);
            }
        }
    }

    public static void setViewsVisible(int visible, View... views) {
        for (View view : views) {
            if (view != null && view.getVisibility() != visible) {
                view.setVisibility(visible);
            }
        }
    }

    public static void setViewsEnable(boolean enable, View... views) {
        for (View view : views) {
            if (view != null) {
                view.setEnabled(enable);
            }
        }
    }

    public static void setTextViewColor(@ColorInt int color, TextView... views) {
        for (TextView view : views) {
            if (view != null) {
                view.setTextColor(color);
            }
        }
    }

    public static void setTextViewColorWithColorRes(@ColorRes int color, TextView... views) {
        setTextViewColor(UIHelper.getColor(color), views);
    }

    public static void setViewsEnableAndClickable(boolean enable, boolean clickable, View... views) {
        for (View view : views) {
            if (view != null) {
                view.setEnabled(enable);
                view.setClickable(clickable);
            }
        }
    }

    /**
     * 扩大View的触摸和点击响应范围,最大不超过其父View范围（ABSListView无效）
     * 延迟300毫秒执行，使该方法可以在onCreate里面使用
     */
    public static void expandViewTouchDelegate(final View view, final int top, final int bottom, final int left, final int right) {
        if (view == null) {
            return;
        }
        if (view.getParent() != null) {

            ((View) view.getParent()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Rect bounds = new Rect();
                    view.setEnabled(true);
                    view.getHitRect(bounds);

                    bounds.top -= top;
                    bounds.bottom += bottom;
                    bounds.left -= left;
                    bounds.right += right;

                    LogHelper.trace("扩大触摸面积 >>> rect - top" + bounds.top + "  - right" + bounds.right);

                    TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                    if (View.class.isInstance(view.getParent())) {
                        ((View) view.getParent()).setTouchDelegate(touchDelegate);
                    }
                }
            }, 300);
        }
    }

    /**
     * 还原View的触摸和点击响应范围,最小不小于View自身范围
     */
    public static void restoreViewTouchDelegate(final View view) {
        if (view == null) {
            return;
        }

        if (view.getParent() != null) {

            ((View) view.getParent()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Rect bounds = new Rect();
                    bounds.setEmpty();
                    TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                    if (View.class.isInstance(view.getParent())) {
                        ((View) view.getParent()).setTouchDelegate(touchDelegate);
                    }
                }
            }, 300);
        }
    }


    public static void setViewsFocusListener(View.OnFocusChangeListener listener, View... views) {
        for (View view : views) {
            if (view != null) {
                view.setOnFocusChangeListener(listener);
            }
        }
    }

    public static void setEditTextWatcher(TextWatcher watcher, TextView... views) {
        for (TextView view : views) {
            if (view != null) {
                view.addTextChangedListener(watcher);
            }
        }
    }

    public static void setTextViewDrawable(TextView textView, @DrawableRes int start, @DrawableRes int top, @DrawableRes int end, @DrawableRes int bottom) {
        if (textView == null) return;
        final Context context = textView.getContext();
        textView.setCompoundDrawablesWithIntrinsicBounds(
                start != 0 ? context.getResources().getDrawable(start) : null,
                top != 0 ? context.getResources().getDrawable(top) : null,
                end != 0 ? context.getResources().getDrawable(end) : null,
                bottom != 0 ? context.getResources().getDrawable(bottom) : null);
    }

    public static void applyAlphaScaleInLayoutAnimation(ViewGroup... viewGroup) {
        for (ViewGroup group : viewGroup) {
            applyAlphaScaleInLayoutAnimationInternal(group);
        }

    }

    static void applyAlphaScaleInLayoutAnimationInternal(ViewGroup viewGroup) {
        if (viewGroup == null) return;
        AnimationSet set = new AnimationSet(false);
        AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
        alpha.setDuration(250);
        alpha.setInterpolator(new DecelerateInterpolator());
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setInterpolator(new DecelerateInterpolator());
        scaleAnimation.setDuration(400);
        set.addAnimation(alpha);
        set.addAnimation(scaleAnimation);
        LayoutAnimationController controller = new CustomLayoutAnimationController(set, 0.3f);   //设置控件显示的顺序；
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);   //设置控件显示间隔时间；
        viewGroup.setLayoutAnimation(controller);
    }

    public static void applyLayoutAnimaOnViewChange(ViewGroup... viewGroup) {
        if (viewGroup == null) return;
        for (ViewGroup group : viewGroup) {
            applyLayoutAnimaOnViewChangeInternal(group);
        }
    }

    static void applyLayoutAnimaOnViewChangeInternal(ViewGroup viewGroup) {
        if (viewGroup == null || Build.VERSION.SDK_INT < 11) return;
        LayoutTransition mTransitioner = new LayoutTransition();
//        setupCustomAnimations(viewGroup, mTransitioner);
        viewGroup.setLayoutTransition(mTransitioner);
    }

    public static void setTextViewDefaultText(TextView... textViews) {
        setTextViewDefaultText("--", textViews);
    }

    public static void setTextViewDefaultText(CharSequence defaultText, TextView... textViews) {
        for (TextView textView : textViews) {
            textView.setText(defaultText);
        }
    }

    public static void justifyTextView(final TextView tv) {
        if (tv == null) return;
        TextPaint textPaint = tv.getPaint();
        if (textPaint == null) {
            textPaint = new TextPaint();
            textPaint.setTextSize(tv.getTextSize());
            textPaint.setTextScaleX(tv.getTextScaleX());
        }
        final TextPaint finalTextPaint = textPaint;
        tv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                //获取行数和宽度
                final int lineCount = tv.getLineCount();
                final int viewWidth = tv.getWidth();

                final CharSequence originCharSequence = tv.getText();
                if (TextUtils.isEmpty(originCharSequence)) {
                    return true;
                }
                if (tv.getLayout() == null) return true;
                SpannableStringBuilder builder = new SpannableStringBuilder();

                for (int i = 0; i < lineCount; i++) {
                    //截取字符
                    int lineStart = tv.getLayout().getLineStart(i);
                    int lineEnd = tv.getLayout().getLineEnd(i);
                    //原每行的字符串
                    CharSequence eachLineSequence = originCharSequence.subSequence(lineStart, lineEnd);
                    if (i == lineCount - 1) {
                        builder.append(eachLineSequence);
                        break;
                    }

                    //去掉首尾空格保证两端对齐
                    CharSequence eachLineTrimSequence = StringUtil.trim(eachLineSequence);

                }

                return true;
            }
        });
    }


    private static class CharSequenceHolderSpan extends ReplacementSpan {


        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
            return 0;
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {

        }
    }

    public static void adjustEditTextSelection(EditText... editTexts) {
        if (editTexts != null) {
            for (EditText editText : editTexts) {
                editText.setSelection(editText.getText().length());
            }
        }
    }


    public static void shakeView(View view, long duration) {
        if (view == null) return;
        TranslateAnimation shakeAnimation = new TranslateAnimation(0, RandomUtil.random(-15,15), 0, RandomUtil.random(-20,20));
        shakeAnimation.setDuration(duration);
        shakeAnimation.setInterpolator(new CycleInterpolator(10));
        view.startAnimation(shakeAnimation);
    }

}
