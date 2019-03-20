package com.zjzf.shoescircle.ui.widget.dialog;

/**
 * Created by 陈志远 on 2017/5/3.
 * <p>
 * 默认negativeclick返回true，也就是点击dismiss的simple listener
 */
public abstract class SimpleDialogButtonClickListener implements DialogButtonClickListener {
    @Override
    public boolean onNegativeClicked() {
        return true;
    }
}
