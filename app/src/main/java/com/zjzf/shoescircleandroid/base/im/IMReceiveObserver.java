package com.zjzf.shoescircleandroid.base.im;

import io.rong.imlib.model.Message;

/**
 * Created by 陈志远 on 2018/11/25.
 */
public interface IMReceiveObserver {
    public void onReceived(Message message, int i);
}
