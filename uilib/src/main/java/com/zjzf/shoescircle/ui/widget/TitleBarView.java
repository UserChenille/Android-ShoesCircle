package com.zjzf.shoescircle.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzf.shoescircle.lib.interfaces.MultiClickListener;
import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircle.lib.utils.MultiSpanUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.utils.ViewUtil;
import com.zjzf.shoescircle.uilib.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.zjzf.shoescircle.ui.widget.TitleBarView.TitleBarMode.MODE_BOTH;
import static com.zjzf.shoescircle.ui.widget.TitleBarView.TitleBarMode.MODE_LEFT;
import static com.zjzf.shoescircle.ui.widget.TitleBarView.TitleBarMode.MODE_NONE;
import static com.zjzf.shoescircle.ui.widget.TitleBarView.TitleBarMode.MODE_RIGHT;


/**
 * Created by 陈志远 on 2017/5/15.
 * <p>
 * 通用性titlebar
 * <p>
 * <declare-styleable name="TitleBarView">
 * <attr name="icon_left" format="reference"/>
 * <attr name="icon_right" format="reference"/>
 * <attr name="mode" format="enum">
 * <enum name="mode_left" value="16"/>
 * <enum name="mode_right" value="17"/>
 * <enum name="mode_both" value="18"/>
 * <enum name="mode_none" value="19"/>
 * </attr>
 * <attr name="transparentMode" format="boolean"/>
 * <attr name="title_text_color" format="color"/>
 * <attr name="title_text_size" format="dimension"/>
 * <attr name="title_text" format="string"/>
 * </declare-styleable>
 */

public class TitleBarView extends FrameLayout implements View.OnClickListener {
    private static final String TAG = "TitleBarView";

    protected static final int DEFAULT_TITLE_BAR_BACKGOUND = Color.WHITE;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_LEFT, MODE_RIGHT, MODE_BOTH, MODE_NONE})
    public @interface TitleBarMode {
        int MODE_LEFT = 16;
        int MODE_RIGHT = 17;
        int MODE_BOTH = 18;
        int MODE_NONE = 19;
    }

    protected int mode = MODE_LEFT;

    protected String leftText;
    protected String rightText;
    @DrawableRes
    protected int leftIcon;
    @DrawableRes
    protected int rightIcon;
    protected boolean isTransparent;
    @ColorInt
    protected int mainTextColor;
    protected int mainTextSize;
    protected String mainText;

    @ColorInt
    protected int leftTextColor;
    @ColorInt
    protected int rightTextColor;
    protected int leftTextSize;
    protected int rightTextSize;
    private int rightBtnPadding;

    protected ImageView btn_left;
    protected ImageView btn_right;
    protected LinearLayout layout_left;
    protected LinearLayout layout_right;
    protected TextView text_left;
    protected TextView text_right;
    protected TextView tv_title;
    protected RelativeLayout titlebar_root;

    protected OnTitlebarClickCallback mTitlebarClickCallback;

    Drawable background = null;

    public TitleBarView(@NonNull Context context) {
        this(context, null);
    }

    public TitleBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setId(R.id.title_bar_view);
        initFromAttrs(context, attrs);
        initView(context);
    }

    protected void initFromAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView);
        background = a.getDrawable(R.styleable.TitleBarView_android_background);
        isTransparent = a.getBoolean(R.styleable.TitleBarView_transparentMode, false);
        leftIcon = a.getResourceId(R.styleable.TitleBarView_icon_left, isTransparent ? R.drawable.arrow_left_black : R.drawable.arrow_left_black);
        rightIcon = a.getResourceId(R.styleable.TitleBarView_icon_right, 0);
        leftText = a.getString(R.styleable.TitleBarView_text_left);
        rightText = a.getString(R.styleable.TitleBarView_text_right);
        mode = a.getInt(R.styleable.TitleBarView_mode, MODE_LEFT);
        mainTextSize = a.getDimensionPixelSize(R.styleable.TitleBarView_title_text_size, 18);
        mainTextColor = a.getColor(R.styleable.TitleBarView_title_text_color, ContextCompat.getColor(context, R.color.common_black));

        leftTextSize = a.getDimensionPixelSize(R.styleable.TitleBarView_left_text_size, 16);
        leftTextColor = a.getColor(R.styleable.TitleBarView_left_text_color, ContextCompat.getColor(context, R.color.common_black));

        rightTextSize = a.getDimensionPixelSize(R.styleable.TitleBarView_right_text_size, 16);
        rightTextColor = a.getColor(R.styleable.TitleBarView_right_text_color, ContextCompat.getColor(context, R.color.common_black));
        rightBtnPadding = a.getDimensionPixelSize(R.styleable.TitleBarView_right_btn_padding, 0);

        mainText = a.getString(R.styleable.TitleBarView_title_text);
        a.recycle();
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_title_bar_view, this);
        this.btn_left = findViewById(R.id.title_bar_icon_left);
        this.btn_right = findViewById(R.id.title_bar_icon_right);
        this.layout_left = findViewById(R.id.title_bar_layout_left);
        this.layout_right = findViewById(R.id.title_bar_layout_right);
        this.text_left = findViewById(R.id.title_bar_text_left);
        this.text_right = findViewById(R.id.title_bar_text_right);
        this.tv_title = findViewById(R.id.title_bar_title);
        this.titlebar_root = findViewById(R.id.title_bar_root);
        this.titlebar_root.setOnClickListener(mMultiClickListener);
        this.titlebar_root.setOnLongClickListener(mLongClickListener);
        ViewUtil.setViewsClickListener(this, layout_left, layout_right);
        setValues();
    }

    private void setValues() {
        setTransparentMode(isTransparent);
        setMode(mode);
        setTitleTextSize(mainTextSize);
        setTitleTextColor(mainTextColor);
        setLeftTextColor(leftTextColor);
        setLeftTextSize(leftTextSize);
        setRightTextColor(rightTextColor);
        setRightTextSize(rightTextSize);
        setTitleText(mainText);
        setLeftIcon(leftIcon);
        setRightIcon(rightIcon);
        setLeftText(leftText);
        setRightText(rightText);
    }


    void onModeChanged() {
        switch (mode) {
            case MODE_BOTH:
                ViewUtil.setViewsVisible(VISIBLE, layout_left, layout_right);
                break;
            case MODE_NONE:
                ViewUtil.setViewsVisible(GONE, layout_left, layout_right);
                break;
            case MODE_LEFT:
                ViewUtil.setViewsVisible(VISIBLE, layout_left);
                ViewUtil.setViewsVisible(GONE, layout_right);
                break;
            case MODE_RIGHT:
                ViewUtil.setViewsVisible(VISIBLE, layout_right);
                ViewUtil.setViewsVisible(GONE, layout_left);
                break;
            default:
                //都不匹配则按照left模式设置
                ViewUtil.setViewsVisible(VISIBLE, layout_left);
                ViewUtil.setViewsVisible(GONE, layout_right);
                break;
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(@TitleBarMode int mode) {
        setModeInternal(mode, true);
    }

    void setModeInternal(@TitleBarMode int mode, boolean callChange) {
        this.mode = mode;
        if (callChange) {
            onModeChanged();
        }
    }

    public int getLeftIcon() {
        return leftIcon;
    }

    public void setLeftIcon(@DrawableRes int leftIcon) {
        this.leftIcon = leftIcon;
        ViewUtil.setViewsVisible(leftIcon != 0 ? VISIBLE : GONE, btn_left);
        if (leftIcon != 0) {
            btn_left.setImageResource(leftIcon);
        }
        adjustTextAndIcon();
    }

    public void setLeftText(@StringRes int leftText) {
        setLeftText(getResources().getString(leftText));
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
        ViewUtil.setViewsVisible(TextUtils.isEmpty(leftText) ? GONE : VISIBLE, text_left);
        text_left.setText(leftText);
        adjustTextAndIcon();
    }

    public void setRightText(@StringRes int rightText) {
        setRightText(getResources().getString(rightText));
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
        ViewUtil.setViewsVisible(TextUtils.isEmpty(rightText) ? GONE : VISIBLE, text_right);
        text_right.setText(rightText);
        adjustTextAndIcon();
    }

    public void setRightTextColor(@ColorInt int textColor) {
        this.rightTextColor = textColor;
        text_right.setTextColor(textColor);
    }

    public void setRightTextSize(int textSize) {
        this.rightTextSize = textSize;
        text_right.setTextSize(textSize);
    }

    public void setLeftTextColor(@ColorInt int textColor) {
        this.leftTextColor = textColor;
        text_left.setTextColor(textColor);
    }

    public void setLeftTextSize(int textSize) {
        this.leftTextSize = textSize;
        text_left.setTextSize(textSize);
    }

    public int getRightIcon() {
        return rightIcon;
    }

    public void setRightIcon(@DrawableRes int rightIcon) {
        this.rightIcon = rightIcon;
        ViewUtil.setViewsVisible(rightIcon != 0 ? VISIBLE : GONE, btn_right);
        if (rightIcon != 0) {
            btn_right.setImageResource(rightIcon);
        }
        adjustTextAndIcon();
    }

    private void adjustTextAndIcon() {
        boolean leftAllPadding = leftIcon == 0 && !TextUtils.isEmpty(leftText);
        boolean rightAllPadding = rightIcon == 0 && !TextUtils.isEmpty(rightText);

        boolean leftSingleSidePadding = leftIcon != 0 && !TextUtils.isEmpty(leftText);
        boolean rightSingleSidePadding = leftIcon != 0 && !TextUtils.isEmpty(leftText);

        text_left.setPadding(leftAllPadding ? dip2px(16) : 0, 0, (leftAllPadding || leftSingleSidePadding) ? dip2px(16) : 0, 0);
        text_right.setPadding((rightAllPadding || rightSingleSidePadding) ? dip2px(16) : 0, 0, rightAllPadding ? dip2px(16) : 0, 0);
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public void setTransparentMode(boolean transparent) {
        if (background != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                titlebar_root.setBackground(background);
            } else {
                titlebar_root.setBackgroundDrawable(background);
            }
            layout_left.setBackgroundResource(R.drawable.bg_trans_btn_s);
            layout_right.setBackgroundResource(R.drawable.bg_trans_btn_s);
            return;
        }
        isTransparent = transparent;
        if (isTransparent) {
            titlebar_root.setBackgroundColor(Color.TRANSPARENT);
        } else {
            titlebar_root.setBackgroundColor(DEFAULT_TITLE_BAR_BACKGOUND);
        }
        if (leftIcon == R.drawable.arrow_left_black || leftIcon == R.drawable.arrow_left_black) {
            setLeftIcon(isTransparent ? R.drawable.arrow_left_black : R.drawable.arrow_left_black);
            layout_left.setBackgroundResource(isTransparent ? R.drawable.bg_trans_btn_s : R.drawable.bg_top_bar_btn_s);
            layout_right.setBackgroundResource(isTransparent ? R.drawable.bg_trans_btn_s : R.drawable.bg_top_bar_btn_s);
        }
    }

    public int getTitleTextColor() {
        return mainTextColor;
    }

    public void setTitleTextColor(@ColorInt int mainTextColor) {
        this.mainTextColor = mainTextColor;
        this.tv_title.setTextColor(mainTextColor);
    }

    public int getTitleTextSize() {
        return mainTextSize;
    }

    public void setTitleTextSize(int pxSize) {
        this.mainTextSize = pxSize;
        this.tv_title.setTextSize(mainTextSize);
    }

    public String getTitleText() {
        return mainText;
    }

    public void setTitleText(@StringRes int mainText) {
        try {
            setTitleText(getResources().getString(mainText));
        } catch (Resources.NotFoundException e) {
            setTitleText("");
        }
    }

    public void setRightBtnPadding(int rightBtnPadding) {
        layout_right.setPadding(0, 0, rightBtnPadding, 0);
    }

    public void setSubTitleText(String subTitleText) {
        setSubTitleText(subTitleText, 12);
    }

    public void setSubTitleText(String subTitleText, int subTextSizeSP) {
        setSubTitleText(subTitleText, mainTextSize, subTextSizeSP);
    }

    public void setSubTitleText(String subTitleText, int mainTextSizeSP, int subTextSizeSP) {
        setSubTitleText(subTitleText, mainTextSizeSP, subTextSizeSP, mainTextColor);
    }

    public void setSubTitleText(String subTitleText, int mainTextSizeSP, int subTextSizeSP, @ColorInt int subTextColor) {
        if (TextUtils.isEmpty(subTitleText)) return;
        if (mainTextSizeSP != this.mainTextSize) {
            setTitleTextSize(sp2px(mainTextSizeSP));
        }
        String allTitleText = this.mainText + '\n' + subTitleText;
        setTitleText(MultiSpanUtil.create(allTitleText)
                .append(subTitleText).setTextSize(subTextSizeSP != 0 ? subTextSizeSP : -1).setTextColor(subTextColor != 0 ? subTextColor : -1)
                .getSpannableStringBuilder());
    }

    public void setTitleText(CharSequence mainText) {
        if (mainText != null) {
            this.mainText = mainText.toString();
            tv_title.setText(mainText);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.title_bar_icon_left || i == R.id.title_bar_layout_left) {
            if (mTitlebarClickCallback != null) {
                mTitlebarClickCallback.onTitleLeftClick();
            }

        } else if (i == R.id.title_bar_icon_right || i == R.id.title_bar_layout_right) {
            if (mTitlebarClickCallback != null) {
                mTitlebarClickCallback.onTitleRightClick();
            }
        }

    }

    private MultiClickListener mMultiClickListener = new MultiClickListener() {
        @Override
        public void onSingleClick() {
            if (mTitlebarClickCallback != null) {
                mTitlebarClickCallback.onTitleClick();
            }
        }

        @Override
        public void onDoubleClick() {
            if (mTitlebarClickCallback != null) {
                mTitlebarClickCallback.onTitleDoubleClick();
            }
        }
    };

    private OnLongClickListener mLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            return mTitlebarClickCallback != null && mTitlebarClickCallback.onTitleLongClick();
        }
    };

    public OnTitlebarClickCallback getTitlebarClickCallback() {
        return mTitlebarClickCallback;
    }

    public void setTitlebarClickCallback(OnTitlebarClickCallback titlebarClickCallback) {
        mTitlebarClickCallback = titlebarClickCallback;
    }

    public interface OnTitlebarClickCallback {
        void onTitleLeftClick();

        void onTitleRightClick();

        void onTitleClick();

        void onTitleDoubleClick();

        boolean onTitleLongClick();

    }

    int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    int sp2px(float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getResources().getDisplayMetrics());
    }

    protected float defaultAlpha = 1f;

    public void bindAlphaChangeView(final View which, final View totalScrollView, @Nullable final OnAlphaChangeListener listener) {
        if (which == null || totalScrollView == null) return;
        totalScrollView.post(new Runnable() {
            @Override
            public void run() {
                bindAlphaChangeView(which, totalScrollView.getHeight(), listener);
            }
        });
    }

    public void bindAlphaChangeView(final View which, final int totalScroll, @Nullable final OnAlphaChangeListener listener) {
        if (which == null || totalScroll <= 0) return;
        defaultAlpha = getAlpha();
        if (background == null) {
            titlebar_root.setBackgroundColor(Color.TRANSPARENT);
        }
        final int endColor = mainTextColor == Color.WHITE ? UIHelper.getColor(R.color.common_black) : mainTextColor;
        which.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                final int scrollY = which.getScrollY();
                LogHelper.trace( "scrollY = " + scrollY);
                if (scrollY > 0) {
                    float alpha = (float) scrollY / totalScroll;
                    if (alpha >= defaultAlpha) {
                        alpha = defaultAlpha;
                    }
                    LogHelper.trace( "alpha = " + alpha);
                    if (listener != null && listener.onAlphaChange(alpha)) return;
                    if (leftIcon == R.drawable.arrow_left_black || leftIcon == R.drawable.arrow_left_black) {
                        setLeftIcon(alpha <= 0.5f ? R.drawable.arrow_left_black : R.drawable.arrow_left_black);
                        layout_left.setBackgroundResource(alpha <= 0.5f ? R.drawable.bg_trans_btn_s : R.drawable.bg_top_bar_btn_s);
                        layout_right.setBackgroundResource(alpha <= 0.5f ? R.drawable.bg_trans_btn_s : R.drawable.bg_top_bar_btn_s);

                        setTitleTextColor(UIHelper.gradientColor(alpha, Color.WHITE, endColor));
                    }
                    titlebar_root.setBackgroundColor(argb(alpha, 1f, 1f, 1f));
                } else {
                    if (listener != null && listener.onAlphaChange(0)) return;
                    titlebar_root.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
    }


    int argb(float alpha, float red, float green, float blue) {
        return ((int) (alpha * 255.0f + 0.5f) << 24) |
                ((int) (red * 255.0f + 0.5f) << 16) |
                ((int) (green * 255.0f + 0.5f) << 8) |
                (int) (blue * 255.0f + 0.5f);
    }

    private int argbInt(
            @IntRange(from = 0, to = 255) int alpha,
            @IntRange(from = 0, to = 255) int red,
            @IntRange(from = 0, to = 255) int green,
            @IntRange(from = 0, to = 255) int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public interface OnAlphaChangeListener {
        /**
         * 返回true则不处理默认的变化
         *
         * @param alpha
         * @return
         */
        boolean onAlphaChange(float alpha);
    }


}
