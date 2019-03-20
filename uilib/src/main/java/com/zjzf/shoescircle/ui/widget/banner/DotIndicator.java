package com.zjzf.shoescircle.ui.widget.banner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 陈志远 on 2017/04/20.
 * <p/>
 * 圆形点点指示器
 */
public class DotIndicator extends View {
    private static final String TAG = "DotIndicator";

    private static final int NORMAL_COLOR = 0x20CCCCCC;
    private static final int SELECTED_COLOR = 0xB2CCCCCC;

    private int mDotNormalColor = NORMAL_COLOR;
    private int mDotSelectedColor = SELECTED_COLOR;

    private Paint mDotNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG) {
        {
            setStyle(Style.FILL);
            setColor(mDotNormalColor);
        }
    };

    private Paint mDotSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG) {
        {
            setStyle(Style.FILL);
            setColor(mDotSelectedColor);
        }
    };

    private boolean isSelected;

    private RectF mRectF = new RectF();

    public DotIndicator(Context context) {
        this(context, null);
    }

    public DotIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        mRectF.set(0, 0, width, height);

        canvas.drawOval(mRectF, isSelected ? mDotSelectedPaint : mDotNormalPaint);
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        invalidate();
    }


    public int getDotNormalColor() {
        return mDotNormalColor;
    }

    public void setDotNormalColor(int dotNormalColor) {
        if (mDotNormalColor == dotNormalColor) return;
        mDotNormalColor = dotNormalColor == 0 ? NORMAL_COLOR : dotNormalColor;
        if (mDotNormalPaint != null) {
            mDotNormalPaint.setColor(mDotNormalColor);
        }
        invalidate();
    }

    public int getDotSelectedColor() {
        return mDotSelectedColor;
    }

    public void setDotSelectedColor(int dotSelectedColor) {
        if (mDotSelectedColor == dotSelectedColor) return;
        mDotSelectedColor = dotSelectedColor == 0 ? SELECTED_COLOR : dotSelectedColor;
        if (mDotSelectedPaint != null) {
            mDotSelectedPaint.setColor(mDotSelectedColor);
        }
        invalidate();
    }
}
