package com.zjzf.shoescircle.lib.net.client;

import android.content.Context;
import android.text.TextUtils;

import com.zjzf.shoescircle.lib.net.retrofit.HttpLoggingInterceptor;
import com.zjzf.shoescircle.lib.tools.gson.GsonUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 陈志远 on 2018/7/20.
 */
public enum NetClient {
    INSTANCE;

    private OkHttpClient okHttpClient;
    public static String sUserToken;
    private static final int DEFAULT_TIME_OUT = 10;

    public OkHttpClient getClient(Context context) {
        if (okHttpClient == null) {
            initOkHttpClient(context);
        }
        return okHttpClient;
    }

    private void initOkHttpClient(Context context) {
        if (okHttpClient != null) return;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        okhttp3.Request.Builder request = chain.request()
                                .newBuilder();
                        if (!TextUtils.isEmpty(sUserToken)) {
                            request.addHeader("access-token", sUserToken);
                        }
                        if (TextUtils.equals(chain.request().method(), "POST")) {
                            request.addHeader("Content-Type", "application/json");
                        }
                        return chain.proceed(request.build());
                    }
                })
                .addInterceptor(logging)
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    public static JsonBodyBuilder requestBody(){
        return new JsonBodyBuilder();
    }

    public static class JsonBodyBuilder {
        private HashMap<String, Object> map;

        public JsonBodyBuilder put(String key, Object value) {
            if (map == null) map = new HashMap<>();
            map.put(key, String.valueOf(value));
            return this;
        }

        public RequestBody build() {
            return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), GsonUtil.INSTANCE.toString(map));
        }


        private String encode(final String content, final String encoding) {
            try {
                return URLEncoder.encode(content, encoding != null ? encoding : "UTF-8");
            } catch (UnsupportedEncodingException problem) {
                throw new IllegalArgumentException(problem);
            }
        }
    }
}
