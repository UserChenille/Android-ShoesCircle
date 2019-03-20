package com.zjzf.shoescircle.ui.widget.dialog;

/**
 * Created by 陈志远 on 2017/5/3.
 *
 * dialog两个button点击事件
 */
public interface DialogButtonClickListener {

    /**
     * @return true for dismiss
     */
    boolean onPositiveClicked();

    /**
     * @return true for dismiss
     */
    boolean onNegativeClicked();
}
