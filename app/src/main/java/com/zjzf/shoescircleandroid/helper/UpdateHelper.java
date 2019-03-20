package com.zjzf.shoescircleandroid.helper;

import android.app.Activity;

import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircleandroid.model.UpdateInfo;
import com.zjzf.shoescircleandroid.widget.popup.PopupUpdate;

import com.zjzf.shoescircle.lib.interfaces.SimpleCallback;
import com.zjzf.shoescircle.lib.utils.VersionUtil;


/**
 * Created by 陈志远 on 2017/7/19.
 */

public class UpdateHelper {
    private static final String TAG = "UpdateHelper";

    private Activity mActivity;

    public UpdateHelper(Activity activity) {
        mActivity = activity;
    }

    public void checkUpdate() {
        new UpdateRequest().checkUpdate(new SimpleCallback<UpdateInfo>() {
            @Override
            public void onCall(UpdateInfo data) {
                if (data != null) {
                    int code = data.getBuild();
                    int currentCode = VersionUtil.getAppVersionCode();

                    if (code <= currentCode) {
                        LogHelper.trace("当前已经是最新版");
                    } else {
                        LogHelper.trace("检查到更新");
                        showUpdatePopup(data);
                    }

                }
            }
        });
    }

    public void showUpdatePopup(UpdateInfo response) {
        PopupUpdate popupUpdate = new PopupUpdate(mActivity);
        popupUpdate.showPopupWindow(response);
    }
}
