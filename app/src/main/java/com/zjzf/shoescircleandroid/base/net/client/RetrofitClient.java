package com.zjzf.shoescircleandroid.base.net.client;

import android.text.TextUtils;

import com.zjzf.shoescircle.lib.base.AppContext;
import com.zjzf.shoescircle.lib.helper.RxHelper;
import com.zjzf.shoescircle.lib.net.client.NetClient;
import com.zjzf.shoescircle.lib.net.retrofit.HttpLoggingInterceptor;
import com.zjzf.shoescircle.lib.tools.gson.GsonUtil;
import com.zjzf.shoescircleandroid.base.net.apis.DefaultApis;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static volatile Retrofit mRestfulRetrofit;

    private static final String DEFAULT_BASE_URL = "http://123.56.0.196:8080";

    /**
     * 获取 restful style 的 retrofit
     *
     * @return
     */
    public static Retrofit get() {
        if (mRestfulRetrofit == null) {
            synchronized (RetrofitClient.class) {
                if (mRestfulRetrofit == null) {
                    mRestfulRetrofit = new Retrofit.Builder()
                            .client(NetClient.INSTANCE.getClient(AppContext.getAppContext()))
                            .baseUrl(DEFAULT_BASE_URL)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create(GsonUtil.INSTANCE.getGson()))
                            .build();
                }

            }
        }
        return mRestfulRetrofit;
    }

    public static Retrofit createNewClient(String baseurl) {
        return new Retrofit.Builder()
                .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)).build())
                .baseUrl(TextUtils.isEmpty(baseurl) ? DEFAULT_BASE_URL : baseurl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonUtil.INSTANCE.getGson()))
                .build();
    }

    public static void setInvalidate() {
        synchronized (RetrofitClient.class) {
            mRestfulRetrofit = null;
        }
    }

    public static DefaultApis defaultApi() {
        return get().create(DefaultApis.class);
    }


    public static <T> ObservableTransformer<T, T> defaultTransformer(){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .doOnNext(new HandleServerApiErrorAction<>())
                        .compose(RxHelper.<T>io_main());
            }
        };
    }
}
