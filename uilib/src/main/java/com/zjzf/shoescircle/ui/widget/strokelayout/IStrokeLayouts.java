package com.zjzf.shoescircle.ui.widget.strokelayout;

/**
 * Created by 陈志远 on 2017/11/23.
 */

interface IStrokeLayouts extends ExcludeSide{


    void setExcludeSide(int excludeSide);

    int getExcludeSide();

    void setStrokeWidth(float strokeWidth);

    float getStrokeWidth();

    void setStrokeColor(int strokeColor);

    int getStrokeColor();
}
