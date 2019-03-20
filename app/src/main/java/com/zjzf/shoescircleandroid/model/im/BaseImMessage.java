package com.zjzf.shoescircleandroid.model.im;

import com.zjzf.shoescircle.lib.base.baseadapter.MultiType;

/**
 * Created by 陈志远 on 2018/11/25.
 */
public class BaseImMessage implements MultiType {
    public static final int TYPE_OTHER=1;
    private int type;

    public BaseImMessage(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
