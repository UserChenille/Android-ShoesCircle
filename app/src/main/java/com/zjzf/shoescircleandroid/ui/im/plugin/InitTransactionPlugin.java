package com.zjzf.shoescircleandroid.ui.im.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.zjzf.shoescircle.lib.helper.RxHelper;
import com.zjzf.shoescircle.lib.interfaces.SimpleCallback;
import com.zjzf.shoescircle.lib.utils.CacheRunnable;
import com.zjzf.shoescircleandroid.R;
import com.zjzf.shoescircleandroid.base.manager.UserInfoManager;
import com.zjzf.shoescircleandroid.base.net.apis.UserApis;
import com.zjzf.shoescircleandroid.base.net.client.ApiException;
import com.zjzf.shoescircleandroid.base.net.client.HandleServerApiErrorAction;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.base.router.ActivityLauncher;
import com.zjzf.shoescircleandroid.model.GoodsDetailInfo;
import com.zjzf.shoescircleandroid.model.UserDetailInfo;
import com.zjzf.shoescircleandroid.ui.other.BindAliPayActivity;
import com.zjzf.shoescircleandroid.ui.transaction.OpenTransactionActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imkit.plugin.IPluginModule;

/**
 * Created by 陈志远 on 2018/12/9.
 */
public class InitTransactionPlugin implements IPluginModule {
    private CacheRunnable mCacheRunnable;

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.selector_ic_init_transaction);
    }

    @Override
    public String obtainTitle(Context context) {
        return "发起交易";
    }

    @Override
    public void onClick(final Fragment fragment, final RongExtension rongExtension) {
        UserInfoManager.INSTANCE.getUserInfo().getUserDetailInfo(new SimpleCallback<UserDetailInfo>() {
            @Override
            public void onCall(UserDetailInfo data) {
                if (TextUtils.isEmpty(data.getAliPayAccount())) {
                    mCacheRunnable = new CacheRunnable(new Runnable() {
                        @Override
                        public void run() {
                            toTransaction(fragment);
                        }
                    });
                    rongExtension.startActivityForPluginResult(new Intent(fragment.getContext(), BindAliPayActivity.class),100,InitTransactionPlugin.this);
                } else {
                    toTransaction(fragment);
                }
            }
        });

    }

    private void toTransaction(final Fragment fragment) {
        Intent intent = fragment.getActivity().getIntent();
        Bundle bundle = intent == null ? null : intent.getExtras();
        final OpenTransactionActivity.Option option = new OpenTransactionActivity.Option();
        if (bundle != null && bundle.containsKey("goodsDetail")) {
            GoodsDetailInfo goodsDetailInfo = bundle.getParcelable("goodsDetail");
            if (goodsDetailInfo != null) {
                option.setProductName(goodsDetailInfo.getName())
                        .setProductNo(goodsDetailInfo.getFreightNo())
                        .setSizes(goodsDetailInfo.getSizeList());
                if (goodsDetailInfo.getMember() != null) {
                    GoodsDetailInfo.MemberInfo member = goodsDetailInfo.getMember();
                    option.setOtherAvatar(member.getAvatar())
                            .setOtherId(member.getId())
                            .setOtherName(member.getMemName());
                }
            }
            ActivityLauncher.startToOpenTransactionActivity(fragment.getContext(), option);
        } else {
            if (fragment instanceof UriFragment) {
                Uri uri = ((UriFragment) fragment).getUri();
                if (uri != null) {
                    final String targetId = uri.getQueryParameter("targetId");
                    RetrofitClient.get()
                            .create(UserApis.class)
                            .getUserById(targetId)
                            .doOnNext(new HandleServerApiErrorAction<>())
                            .compose(RxHelper.<ObjectData<UserDetailInfo>>io_main())
                            .subscribe(new OnResponseListener<ObjectData<UserDetailInfo>>() {
                                @Override
                                public void onNext(ObjectData<UserDetailInfo> result) {
                                    if (result.getData() == null) return;
                                    option.setOtherAvatar(result.getData().getAvatar())
                                            .setOtherId(targetId)
                                            .setOtherName(result.getData().getMemName());
                                    ActivityLauncher.startToOpenTransactionActivity(fragment.getContext(), option);
                                }

                                @Override
                                public void onFailure(ApiException e, int code, String message) {
                                    super.onFailure(e, code, message);
                                }
                            });
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (mCacheRunnable != null) {
                mCacheRunnable.restore();
            }
        }
    }
}
