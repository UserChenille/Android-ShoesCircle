package com.zjzf.shoescircleandroid.base.net.apis;

import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.model.SingleStringData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DefaultApis {
    /**
     * 搜索框自动完成
     */
    @GET("/xqGoods/queryAutoComplete")
    Observable<ObjectData<List<SingleStringData>>> autoSearch(@Query("name") String key);

}
