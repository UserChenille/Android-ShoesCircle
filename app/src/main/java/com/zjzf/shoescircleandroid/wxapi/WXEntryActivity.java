
package com.zjzf.shoescircleandroid.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.zjzf.shoescircle.lib.utils.UIHelper;
import com.zjzf.shoescircleandroid.base.BaseActivity;
import com.zjzf.shoescircleandroid.base.api.thirdlogin.ThirdLoginManager;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ThirdLoginManager.INSTANCE.getWechatManager().handleIntent(getIntent(), this);
    }

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected void onPrepareInit(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onInitView(View decorView) {

    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result = null;

        boolean handled = ThirdLoginManager.INSTANCE.handleWechatResp(baseResp);

        if (!handled) {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    result = "";
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = "用户取消";
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = "授权失败";
                    break;
                default:
                    result = "";
                    break;
            }
            if (!TextUtils.isEmpty(result)) {
                UIHelper.toast(result);
            }
        }

        finish();
    }
}
