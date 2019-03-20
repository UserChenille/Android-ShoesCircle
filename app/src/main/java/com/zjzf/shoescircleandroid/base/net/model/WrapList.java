package com.zjzf.shoescircleandroid.base.net.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author lneartao
 * @date 2018/7/3.
 */
public final class WrapList<T> {

    /**
     * offset : 0
     * limit : 2147483647
     * size : 1
     * pages : 8
     * current : 1
     * searchCount : true
     * openSort : true
     * orderByField : id
     * records : [{"id":1015889815853224000,"enable":1,"createTime":"2018-07-08 09:29:37","memId":1,"member":{"id":1,"enable":1,"createTime":"2018-07-08 08:46:54","account":"hehaoyang","phone":"15857193035","memName":"何昊阳","avatar":"","sex":0,"evaluate":5,"score":0,"level":1},"name":"乔丹6","photo":"tfhf23121312","freightNo":"AA223AA5","size":"41,42","price":111,"state":1}]
     * condition : null
     * asc : true
     * offsetCurrent : 0
     */

    @SerializedName("offset")
    private int mOffset;
    @SerializedName("limit")
    private int mLimit;
    @SerializedName("size")
    private int mSize;
    @SerializedName("pages")
    private int mPages;
    @SerializedName("current")
    private int mCurrent;
    @SerializedName("searchCount")
    private boolean mSearchCount;
    @SerializedName("openSort")
    private boolean mOpenSort;
    @SerializedName("orderByField")
    private String mOrderByField;
    @SerializedName("condition")
    private Object mCondition;
    @SerializedName("asc")
    private boolean mAsc;
    @SerializedName("offsetCurrent")
    private int mOffsetCurrent;
    @SerializedName("records")
    private List<T> mRecords;


    public int getOffset() {
        return mOffset;
    }

    public int getLimit() {
        return mLimit;
    }

    public int getSize() {
        return mSize;
    }

    public int getPages() {
        return mPages;
    }

    public int getCurrent() {
        return mCurrent;
    }


    public boolean isSearchCount() {
        return mSearchCount;
    }


    public boolean isOpenSort() {
        return mOpenSort;
    }


    public String getOrderByField() {
        return mOrderByField;
    }


    public Object getCondition() {
        return mCondition;
    }


    public boolean isAsc() {
        return mAsc;
    }


    public int getOffsetCurrent() {
        return mOffsetCurrent;
    }


    public List<T> getList() {
        return mRecords;
    }
}
