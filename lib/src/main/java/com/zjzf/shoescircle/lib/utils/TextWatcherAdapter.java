package com.zjzf.shoescircle.lib.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by 陈志远 on 2018/4/12.
 */
public abstract class TextWatcherAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
