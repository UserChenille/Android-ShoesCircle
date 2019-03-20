package com.zjzf.shoescircle.lib.base.baseadapter;

import java.io.Serializable;

/**
 * Created by 陈志远 on 2018/4/11.
 */
public class SimpleMultiType implements Serializable, MultiType {
    int type;

    public SimpleMultiType() {
    }

    public SimpleMultiType(int type) {
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
