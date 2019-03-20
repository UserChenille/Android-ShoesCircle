package com.zjzf.shoescircle.ui.widget.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.text.TextUtils;

/**
 * Created by 陈志远 on 2017/8/21.
 */

public class ShapeDrawableWithText extends ShapeDrawable {
    Paint textPaint;
    private Builder mBuilder;

    private ShapeDrawableWithText(Builder builder) {
        super(builder.getShape());
        this.mBuilder = builder;
        init();
    }


    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(mBuilder.getTextColor());
        if (mBuilder.getTextSize() != 0) {
            textPaint.setTextSize(mBuilder.getTextSize());
        }
        if (mBuilder.shapeColor != -1) {
            getPaint().setColor(mBuilder.shapeColor);
        }
    }

    public void setText(String text) {
        mBuilder.setText(text);
    }

    public void setBackgrounColor(int color) {
        getPaint().setColor(color);
    }

    @Override
    protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
        super.onDraw(shape, canvas, paint);
        if (!TextUtils.isEmpty(mBuilder.getText())) {
            Rect r = getBounds();
            if (mBuilder.getTextSize() == 0) {
                int textSize = (int) (r.width() * 0.5);
                textPaint.setTextSize(textSize);
            }
            //保证文字居中
            Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
            int baseline = r.top + (r.bottom - r.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mBuilder.getText(), r.centerX(), baseline, textPaint);
        }
    }

    public static class Builder {
        String text;
        int textColor;
        int textSize;
        Shape s;
        int shapeColor = -1;

        public String getText() {
            return text;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public int getTextColor() {
            return textColor;
        }

        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public int getTextSize() {
            return textSize;
        }

        public Builder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Shape getShape() {
            return s;
        }

        public Builder setShape(Shape s) {
            this.s = s;
            return this;
        }

        public int getShapeColor() {
            return shapeColor;
        }

        public Builder setShapeColor(int shapeColor) {
            this.shapeColor = shapeColor;
            return this;
        }

        public ShapeDrawableWithText build() {
            return new ShapeDrawableWithText(this);
        }

        public ShapeDrawableWithText buildCircleShape() {
            OvalShape rr = new OvalShape();
            setShape(rr);
            return new ShapeDrawableWithText(this);
        }

        public ShapeDrawableWithText buildRoundRectShape(final float radius) {
            float[] outerRect = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
            RoundRectShape rr = new RoundRectShape(outerRect, null, null);
            setShape(rr);
            return new ShapeDrawableWithText(this);
        }
    }
}
