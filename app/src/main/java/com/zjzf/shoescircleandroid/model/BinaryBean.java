package com.zjzf.shoescircleandroid.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 陈志远 on 2018/3/8.
 */
class BinaryBean {
    /**
     * fsize : 6446245
     */

    @SerializedName("fsize")
    private int mFsize;

    public int getFsize() {
        return mFsize;
    }

    public void setFsize(int fsize) {
        mFsize = fsize;
    }
}
