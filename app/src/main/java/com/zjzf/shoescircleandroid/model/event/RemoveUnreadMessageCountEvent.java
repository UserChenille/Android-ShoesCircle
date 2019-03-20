package com.zjzf.shoescircleandroid.model.event;

/**
 * Created by 陈志远 on 2019/2/23.
 */
public class RemoveUnreadMessageCountEvent {
    private int count;

    public RemoveUnreadMessageCountEvent() {
    }

    public RemoveUnreadMessageCountEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public RemoveUnreadMessageCountEvent setCount(int count) {
        this.count = count;
        return this;
    }
}
