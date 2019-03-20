package com.zjzf.shoescircleandroid.helper;

import com.zjzf.shoescircle.lib.helper.RxHelper;
import com.zjzf.shoescircle.lib.interfaces.SimpleCallback;
import com.zjzf.shoescircleandroid.base.Define;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.model.UpdateInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 陈志远 on 2018/3/8.
 * https://fir.im/docs/version_detection
 */
public class UpdateRequest {

    private static final String BASE_URL = "http://api.fir.im";

    public void checkUpdate(final SimpleCallback<UpdateInfo> listener) {
        RetrofitClient.createNewClient(BASE_URL)
                .create(Update.class)
                .checkUpdate(Define.FIR_TOKEN)
                .compose(RxHelper.<UpdateInfo>io_main())
                .subscribe(new OnResponseListener<UpdateInfo>() {
                    @Override
                    public void onNext(UpdateInfo result) {
                        if (listener != null) {
                            listener.onCall(result);
                        }
                    }
                });

    }

    interface Update {
        @GET("/apps/latest/5c8b6bae548b7a7f1cdf2923")
        Observable<UpdateInfo> checkUpdate(@Query("api_token") String token);
    }
}
