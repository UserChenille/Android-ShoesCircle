package com.zjzf.shoescircle.ui.widget.textview.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;

import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircle.ui.widget.textview.Type;
import com.zjzf.shoescircle.uilib.R;


/**
 * Created by 陈志远 on 2018/3/8.
 * <p>
 * 普益投通用button，囊括大多数的场景
 */
public class ExButton extends android.support.v7.widget.AppCompatButton implements Type {
    private static final String TAG = "ExButton";

    int normalTextColor = Color.WHITE;
    int pressedTextColor = UIHelper.getColor(R.color.btn_disable);
    int disableTextColor = UIHelper.getColor(R.color.btn_disable);

    int normalBackgroundColor = UIHelper.getColor(R.color.common_blue);
    int pressedBackgroundColor;
    int disableBackgroundColor = Color.parseColor("#e5e5e5");

    boolean strokeMode;
    int strokeWidth = UIHelper.dip2px(0.5f);

    int radius = 0;
    int topLeftRadius = 0;
    int topRightRadius = 0;
    int bottomLeftRadius = 0;
    int bottomRightRadius = 0;

    private int type = NORMAL;


    public ExButton(Context context) {
        this(context, null);
    }

    public ExButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public ExButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExButton);
        Drawable background = null;
        background = a.getDrawable(R.styleable.ExButton_android_background);
        if (background != null) {
            type = NORMAL;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(background);
            } else {
                setBackgroundDrawable(background);
            }
            a.recycle();
            return;
        } else {
            applyAttrs(context, a);
        }
        a.recycle();
    }

    void applyAttrs(Context context, TypedArray a) {
        type = a.getInt(R.styleable.ExButton_type, RECTANGE);
        normalTextColor = a.getColor(R.styleable.ExButton_android_textColor, normalTextColor);
        pressedTextColor = a.getColor(R.styleable.ExButton_textPressedColor, normalTextColor);
        disableTextColor = a.getColor(R.styleable.ExButton_textDisableColor, disableTextColor);

        normalBackgroundColor = a.getColor(R.styleable.ExButton_backgroundColor, normalBackgroundColor);
        pressedBackgroundColor = a.getColor(R.styleable.ExButton_backgroundPressedColor, brightnessColor(normalBackgroundColor, 1.1f));
        disableBackgroundColor = a.getColor(R.styleable.ExButton_backgroundDisableColor, disableBackgroundColor);

        strokeMode = a.getBoolean(R.styleable.ExButton_strokeMode, false);
        strokeWidth = a.getDimensionPixelSize(R.styleable.ExButton_stroke_Width, strokeWidth);

        radius = a.getDimensionPixelOffset(R.styleable.ExButton_corner_radius, radius);
        topLeftRadius = a.getDimensionPixelOffset(R.styleable.ExButton_corner_topLeftRadius, topLeftRadius);
        topRightRadius = a.getDimensionPixelOffset(R.styleable.ExButton_corner_topRightRadius, topRightRadius);
        bottomLeftRadius = a.getDimensionPixelOffset(R.styleable.ExButton_corner_bottomLeftRadius, bottomLeftRadius);
        bottomRightRadius = a.getDimensionPixelOffset(R.styleable.ExButton_corner_bottomRightRadius, bottomRightRadius);

        apply();

    }


    private int brightnessColor(int color, @FloatRange(from = 0f) float brightness) {
        float[] hslArray = new float[3];

        ColorUtils.colorToHSL(color, hslArray);
        hslArray[2] = hslArray[2] * brightness;

        return ColorUtils.HSLToColor(hslArray);
    }


    public void apply() {
        int shape = GradientDrawable.RECTANGLE;
        switch (type) {
            case Type.NORMAL:
            case Type.RECTANGE:
                shape = GradientDrawable.RECTANGLE;
                break;
            case Type.ROUND:
                shape = GradientDrawable.OVAL;
                break;
            case Type.ROUND_RECTANGE:
                shape = GradientDrawable.RECTANGLE;
                break;
        }

        GradientDrawable normalDrawable = createNormalDrawable(shape);
        GradientDrawable pressedDrawable = createPressedDrawable(shape);
        GradientDrawable disableDrawable = createDisableDrawable(shape);


        StateListDrawable backgroundStateListDrawable = new StateListDrawable();

        backgroundStateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        backgroundStateListDrawable.addState(new int[]{android.R.attr.state_focused}, pressedDrawable);
        backgroundStateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, disableDrawable);
        backgroundStateListDrawable.addState(new int[]{}, normalDrawable);

        int[][] textColorState = new int[4][];
        textColorState[0] = new int[]{android.R.attr.state_pressed};
        textColorState[1] = new int[]{android.R.attr.state_focused};
        textColorState[2] = new int[]{-android.R.attr.state_enabled};
        textColorState[3] = new int[]{};

        int[] textColors = {pressedTextColor, pressedTextColor, disableTextColor, normalTextColor};


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(backgroundStateListDrawable);
        } else {
            setBackgroundDrawable(backgroundStateListDrawable);
        }
        setTextColor(new ColorStateList(textColorState, textColors));
    }

    private GradientDrawable createNormalDrawable(int shape) {
        GradientDrawable result = new GradientDrawable();
        result.setShape(shape);
        if (strokeMode) {
            result.setColor(Color.TRANSPARENT);
        } else {
            result.setColor(normalBackgroundColor);
        }
        result.setStroke(strokeWidth, normalBackgroundColor);

        if (type == ROUND_RECTANGE) {
            if (radius > 0) {
                result.setCornerRadius(radius);
            } else {
                result.setCornerRadii(new float[]{topLeftRadius,
                        topLeftRadius,
                        topRightRadius,
                        topRightRadius,
                        bottomRightRadius,
                        bottomRightRadius,
                        bottomLeftRadius,
                        bottomLeftRadius
                });
            }
        }
        return result;
    }

    private GradientDrawable createPressedDrawable(int shape) {
        GradientDrawable result = new GradientDrawable();
        result.setShape(shape);

        if (strokeMode) {
            result.setColor(Color.TRANSPARENT);
        } else {
            result.setColor(pressedBackgroundColor);
        }
        result.setStroke(strokeWidth, pressedBackgroundColor);

        if (type == ROUND_RECTANGE) {
            if (radius > 0) {
                result.setCornerRadius(radius);
            } else {
                result.setCornerRadii(new float[]{topLeftRadius,
                        topLeftRadius,
                        topRightRadius,
                        topRightRadius,
                        bottomRightRadius,
                        bottomRightRadius,
                        bottomLeftRadius,
                        bottomLeftRadius
                });
            }
        }
        return result;
    }

    private GradientDrawable createDisableDrawable(int shape) {
        GradientDrawable result = new GradientDrawable();
        result.setShape(shape);
        if (strokeMode) {
            result.setColor(Color.TRANSPARENT);
        } else {
            result.setColor(disableBackgroundColor);
        }
        result.setStroke(strokeWidth, disableBackgroundColor);

        if (type == ROUND_RECTANGE) {
            if (radius > 0) {
                result.setCornerRadius(radius);
            } else {
                result.setCornerRadii(new float[]{topLeftRadius,
                        topLeftRadius,
                        topRightRadius,
                        topRightRadius,
                        bottomRightRadius,
                        bottomRightRadius,
                        bottomLeftRadius,
                        bottomLeftRadius
                });
            }
        }
        return result;
    }

    public int getNormalTextColor() {
        return normalTextColor;
    }

    public ExButton setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
        return this;
    }

    public int getPressedTextColor() {
        return pressedTextColor;
    }

    public ExButton setPressedTextColor(int pressedTextColor) {
        this.pressedTextColor = pressedTextColor;
        return this;
    }

    public int getDisableTextColor() {
        return disableTextColor;
    }

    public ExButton setDisableTextColor(int disableTextColor) {
        this.disableTextColor = disableTextColor;
        return this;
    }

    public int getNormalBackgroundColor() {
        return normalBackgroundColor;
    }

    public ExButton setNormalBackgroundColor(int normalBackgroundColor) {
        this.normalBackgroundColor = normalBackgroundColor;
        pressedBackgroundColor = brightnessColor(normalBackgroundColor, 1.1f);
        return this;
    }

    public int getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public ExButton setPressedBackgroundColor(int pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
        return this;
    }

    public int getDisableBackgroundColor() {
        return disableBackgroundColor;
    }

    public ExButton setDisableBackgroundColor(int disableBackgroundColor) {
        this.disableBackgroundColor = disableBackgroundColor;
        return this;
    }

    public boolean isStrokeMode() {
        return strokeMode;
    }

    public ExButton setStrokeMode(boolean strokeMode) {
        this.strokeMode = strokeMode;
        return this;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public ExButton setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public int getRadius() {
        return radius;
    }

    public ExButton setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public int getTopLeftRadius() {
        return topLeftRadius;
    }

    public ExButton setTopLeftRadius(int topLeftRadius) {
        this.topLeftRadius = topLeftRadius;
        return this;
    }

    public int getTopRightRadius() {
        return topRightRadius;
    }

    public ExButton setTopRightRadius(int topRightRadius) {
        this.topRightRadius = topRightRadius;
        return this;
    }

    public int getBottomLeftRadius() {
        return bottomLeftRadius;
    }

    public ExButton setBottomLeftRadius(int bottomLeftRadius) {
        this.bottomLeftRadius = bottomLeftRadius;
        return this;
    }

    public int getBottomRightRadius() {
        return bottomRightRadius;
    }

    public ExButton setBottomRightRadius(int bottomRightRadius) {
        this.bottomRightRadius = bottomRightRadius;
        return this;
    }

    public int getType() {
        return type;
    }

    public ExButton setType(int type) {
        this.type = type;
        return this;
    }

    @Override
    public void setTextColor(int color) {
        setNormalTextColor(color);
        super.setTextColor(color);
    }
}
