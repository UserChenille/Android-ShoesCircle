package com.zjzf.shoescircleandroid.base.net.client;

import android.text.TextUtils;

import com.zjzf.shoescircleandroid.base.net.model.Result;

import java.util.HashMap;

public class ApiException extends RuntimeException {

    public static final HashMap<Integer, String> ERROR_TABLE = new HashMap<>();

    public static final int SESSION_TIMEOUT = 40102;

    static {
        ERROR_TABLE.put(300, "business error");
        ERROR_TABLE.put(40101, "token error");
        ERROR_TABLE.put(40102, "token expired");
        ERROR_TABLE.put(40103, "user invalid");
        ERROR_TABLE.put(40104, "user locked");
        ERROR_TABLE.put(40331, "forbidden");
        ERROR_TABLE.put(40400, "resource missing");
        ERROR_TABLE.put(40401, "missing parameter");
        ERROR_TABLE.put(40402, "illegal parameter");
        ERROR_TABLE.put(500, "server error");
        ERROR_TABLE.put(40105, "没有绑定支付宝账号");
    }

    private int mErrorCode;
    private Result mResultData;

    public Result getResultData() {
        return mResultData;
    }

    @Override
    public String getMessage() {
        String result = null;
        if (mResultData != null) {
            result = mResultData.getMessage();
        }
        if (TextUtils.isEmpty(result)) {
            result = ERROR_TABLE.get(mErrorCode);
        }
        return TextUtils.isEmpty(result) ? super.getMessage() : result;
    }

    public int getErrorCode() {
        int errorCode = Integer.MAX_VALUE;
        if (mResultData != null) {
            errorCode = mResultData.getErrorCode();
        }
        if (errorCode == Integer.MAX_VALUE) {
            errorCode = mErrorCode;
        }
        return errorCode;
    }

    ApiException(int errorCode, String detailMessage, Result resultData) {
        super(detailMessage);
        mErrorCode = errorCode;
        mResultData = resultData;
    }

}
