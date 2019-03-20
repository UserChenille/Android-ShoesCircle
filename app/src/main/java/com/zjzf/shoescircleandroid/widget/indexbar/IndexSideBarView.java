package com.zjzf.shoescircleandroid.widget.indexbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.zjzf.shoescircle.lib.utils.ToolUtil;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircleandroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈志远 on 2019/3/11.
 */
public class IndexSideBarView extends View {
    private List<String> mLetters = new ArrayList<>();
    private List<Pair<RectF, RectF>> mLettersDrawBounds = new ArrayList<>();

    private int mSelectedIndex = -1;

    private Paint mSelectedBackgroundPaint;
    private Paint mNormalTextPaint;
    private Paint mSelectedTextPaint;
    private Paint mIndicatePaint;
    private Paint mIndicateTextPaint;

    private int mSelectedBackgroundColor = UIHelper.getColor(R.color.colorPrimary);
    private int mNormalTextColor = UIHelper.getColor(R.color.color_black3);
    private int mSelectedTextColor = Color.WHITE;
    private int mIndicateColor = UIHelper.getColor(R.color.colorPrimary);
    private int mIndicateTextColor = Color.WHITE;
    private int mTextSize = UIHelper.sp2px(10);
    private int mIndicateTextSize = UIHelper.sp2px(16);

    private Rect mTextMeasureRect = new Rect();
    private RectF mIndicateRect = new RectF();
    private float textMargin = UIHelper.dip2px(8);
    private int mIndicateRadius = UIHelper.dip2px(30);

    private int mIndicateAlpha = 0;
    private boolean onTouchMode = false;
    private float maxIndicateAlpha = 200;

    private OnSelectedListener mSelectedListener;


    public IndexSideBarView(Context context) {
        this(context, null);
    }

    public IndexSideBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexSideBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray typeArray = getContext().obtainStyledAttributes(attrs, R.styleable.IndexSideBarView, defStyleAttr, 0);
        mSelectedBackgroundColor = typeArray.getColor(R.styleable.IndexSideBarView_idx_selected_background_color, mSelectedBackgroundColor);
        mNormalTextColor = typeArray.getColor(R.styleable.IndexSideBarView_idx_text_color, mNormalTextColor);
        mSelectedTextColor = typeArray.getColor(R.styleable.IndexSideBarView_idx_selected_text_color, mSelectedTextColor);
        mIndicateColor = typeArray.getColor(R.styleable.IndexSideBarView_idx_indicate_background_color, mIndicateColor);
        mIndicateTextColor = typeArray.getColor(R.styleable.IndexSideBarView_idx_indicate_text_color, mIndicateTextColor);
        mTextSize = typeArray.getDimensionPixelOffset(R.styleable.IndexSideBarView_idx_text_size, mTextSize);
        mIndicateTextSize = typeArray.getDimensionPixelOffset(R.styleable.IndexSideBarView_idx_indicate_text_size, mIndicateTextSize);
        typeArray.recycle();

        initPaint();
    }

    private void initPaint() {
        mSelectedBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mSelectedBackgroundPaint.setColor(mSelectedBackgroundColor);
        mSelectedBackgroundPaint.setStyle(Paint.Style.FILL);

        mNormalTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mNormalTextPaint.setColor(mNormalTextColor);
        mNormalTextPaint.setTextSize(mTextSize);

        mSelectedTextPaint = new Paint(mNormalTextPaint);
        mSelectedTextPaint.setColor(mSelectedTextColor);

        mIndicatePaint = new Paint(mSelectedBackgroundPaint);
        mIndicatePaint.setColor(mIndicateColor);
        mIndicatePaint.setAlpha(mIndicateAlpha);

        mIndicateTextPaint = new Paint(mNormalTextPaint);
        mIndicateTextPaint.setColor(mIndicateTextColor);
        mIndicateTextPaint.setTextSize(mIndicateTextSize);
        mIndicateTextPaint.setAlpha(mIndicateAlpha);
    }


    public void setLetters(List<String> letters) {
        if (!ToolUtil.isListEmpty(letters)) {
            mLetters.clear();
            mTextMeasureRect.setEmpty();
            mIndicateRect.setEmpty();
            mLettersDrawBounds.clear();
            mLetters.addAll(letters);
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (ToolUtil.isListEmpty(mLetters)) return;
        if (mTextMeasureRect.isEmpty()) {
            mNormalTextPaint.getTextBounds(mLetters.get(mLetters.size() / 2), 0, 1, mTextMeasureRect);
        }
        if (mLettersDrawBounds.isEmpty()) {
            initLettersDrawBounds();
        }
        if (mIndicateRect.isEmpty()) {
            initIndicateBounds();
        }

        drawLetters(canvas);
        drawSelected(canvas);
        if (onTouchMode) {
            drawTouchIndicate(canvas);
        }
    }

    private void initIndicateBounds() {
        int centerX = (getWidth() - getPaddingLeft() - getPaddingRight()) >> 1;
        int centerY = (getHeight() - getPaddingTop() - getPaddingBottom()) >> 1;

        mIndicateRect.set(centerX - mIndicateRadius,
                centerY - mIndicateRadius,
                centerX + mIndicateRadius,
                centerY + mIndicateRadius);
    }

    private void initLettersDrawBounds() {
        if (!ToolUtil.isListEmpty(mLettersDrawBounds)) return;
        int textLeft = getWidth() - getPaddingRight() - mTextMeasureRect.width();
        int textWidth = mTextMeasureRect.width();
        int textHeight = mTextMeasureRect.height();
        int maxTextHeight = (int) (0.75 * (getHeight() - getPaddingTop() - getPaddingBottom()));
        int textAllHeight = (int) (mTextMeasureRect.height() * mLetters.size() + (textMargin * mLetters.size() - 1));
        int middle = (getHeight() - getPaddingTop() - getPaddingBottom() - maxTextHeight) >> 1;
        int textTop = middle + ((maxTextHeight - textAllHeight) >> 1);

        RectF lastRect = null;
        for (int i = 0; i < mLetters.size(); i++) {
            RectF rect = new RectF();
            String letter = mLetters.get(i);
            mNormalTextPaint.getTextBounds(letter, 0, 1, mTextMeasureRect);
            float l = textLeft + ((textWidth - mTextMeasureRect.width()) >> 1);
            float t = lastRect == null ? textTop : lastRect.bottom + textMargin;
            float r = l + textWidth;
            float b = t + textHeight;
            rect.set(l, t, r, b);
            RectF contentRect = new RectF(rect);
            contentRect.set(textLeft, rect.top, textLeft + textWidth, rect.bottom);
            contentRect.offset(0, -textHeight);
            mLettersDrawBounds.add(Pair.create(rect, contentRect));
            lastRect = rect;
        }
    }

    private void drawLetters(Canvas canvas) {
        for (int i = 0; i < mLetters.size(); i++) {
            Pair<RectF, RectF> mRectPair = mLettersDrawBounds.get(i);
            canvas.drawText(mLetters.get(i), mRectPair.first.left, mRectPair.first.top, mNormalTextPaint);
        }
    }

    private void drawSelected(Canvas canvas) {
        if (mSelectedIndex == -1 || mSelectedIndex >= mLetters.size()) return;
        Pair<RectF, RectF> mRectPair = mLettersDrawBounds.get(mSelectedIndex);
        canvas.drawCircle(mRectPair.second.centerX(), mRectPair.second.centerY(), mRectPair.second.width(), mSelectedBackgroundPaint);
        canvas.drawText(mLetters.get(mSelectedIndex), mRectPair.first.left, mRectPair.first.top, mSelectedTextPaint);
    }

    private void drawTouchIndicate(Canvas canvas) {
        if (mSelectedIndex == -1) return;
        mIndicatePaint.setAlpha(mIndicateAlpha);
        mIndicateTextPaint.setAlpha(mIndicateAlpha);
        canvas.drawRoundRect(mIndicateRect, 8, 8, mIndicatePaint);
        String selected = mLetters.get(mSelectedIndex);
        mIndicateTextPaint.getTextBounds(selected, 0, selected.length(), mTextMeasureRect);
        canvas.drawText(selected,
                mIndicateRect.centerX() - mTextMeasureRect.centerX(),
                mIndicateRect.centerY() - mTextMeasureRect.centerY(),
                mIndicateTextPaint);
    }

    RectF expandClickRectf = new RectF();
    private int findSelected(float x, float y) {
        for (int i = 0; i < mLettersDrawBounds.size(); i++) {
            expandClickRectf.set(mLettersDrawBounds.get(i).second);
            expandClickRectf.inset(-30,-30);
            if (expandClickRectf.contains(x, y)) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int selected = findSelected(x, y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (selected == -1) return false;
                mSelectedIndex = selected;
                onTouchMode = true;
                startAnimator(0f, 1f);
                if (mSelectedListener != null) {
                    mSelectedListener.onSelectedListener(mLetters.get(mSelectedIndex));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (selected == -1 || selected == mSelectedIndex) return false;
                mSelectedIndex = selected;
                invalidate();
                if (mSelectedListener != null) {
                    mSelectedListener.onSelectedListener(mLetters.get(mSelectedIndex));
                }
                break;
            case MotionEvent.ACTION_UP:
                startAnimator(1f, 0f);
                break;
        }
        return true;
    }

    private void startAnimator(float from, final float to) {
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(500);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mIndicateAlpha = (int) (maxIndicateAlpha * ((float) animation.getAnimatedValue()));
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (to == 0f) {
                    onTouchMode = false;
                    invalidate();
                }
            }
        });
        animator.start();

    }

    public OnSelectedListener getOnSelectedListener() {
        return mSelectedListener;
    }

    public IndexSideBarView setOnSelectedListener(OnSelectedListener selectedListener) {
        mSelectedListener = selectedListener;
        return this;
    }

    public interface OnSelectedListener {
        void onSelectedListener(String mSlectedTag);
    }

}
