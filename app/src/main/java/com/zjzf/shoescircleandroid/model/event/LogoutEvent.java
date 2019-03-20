package com.zjzf.shoescircleandroid.model.event;

import com.zjzf.shoescircle.lib.net.client.NetClient;
import com.zjzf.shoescircleandroid.base.manager.RongYunManager;

/**
 * Created by 陈志远 on 2018/10/13.
 */
public class LogoutEvent {
    public LogoutEvent() {
        NetClient.sUserToken = null;
        RongYunManager.INSTANCE.disConnect();
    }
}
