package com.zjzf.shoescircleandroid.base.net.client;

import com.zjzf.shoescircleandroid.base.net.model.BaseResultData;

import io.reactivex.functions.Consumer;

public class HandleServerApiErrorAction<T> implements Consumer<T> {

    @Override
    public void accept(T t) throws Exception {
        if (t instanceof BaseResultData) {
            BaseResultData result = (BaseResultData) t;
            int errorCode = result.getErrorCode();
            if (ApiException.ERROR_TABLE.containsKey(errorCode)) {
                throw new ApiException(errorCode, ApiException.ERROR_TABLE.get(errorCode), result);
            }
        }
    }
}
