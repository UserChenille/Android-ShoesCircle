package com.zjzf.shoescircleandroid.base.net.apis;

import com.zjzf.shoescircleandroid.base.net.model.ObjectData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 陈志远 on 2018/9/29.
 * <p>
 * 第三方api
 */
public interface ThirdApis {
    /**
     * 融云Token
     *
     * @param userId 用户 Id，
     *               支持大小写英文字母、数字、部分特殊符号 + | = - _ 的组合方式，
     *               最大长度 64 字节。是用户在 App 中的唯一标识，
     *               必须保证在同一个 App 内不重复，重复的用户 Id 将被当作是同一用户。（必传）
     * @param name   用户名称，最大长度 128 字节。用来在 Push 推送时显示用户的名称。（必传）
     * @param avatar 用户头像 URI，最大长度 1024 字节。（必传）
     */
    @POST("/user/getToken.json")
    @FormUrlEncoded
    Observable<ObjectData<String>> getRongYunToken(@Field("userId") String userId,
                                                   @Field("name") String name,
                                                   @Field("portraitUri") String avatar);

}
