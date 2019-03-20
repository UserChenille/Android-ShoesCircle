package com.zjzf.shoescircleandroid.model.pay;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈志远 on 2019/3/10.
 */
public class AliPayResult {
    private String resultStatus;
    private String result;
    private String memo;

    static HashMap<String, String> payStatusMessage;

    static {
        //https://docs.open.alipay.com/204/105301/
        payStatusMessage = new HashMap<>();
        payStatusMessage.put("9000", "支付成功");
        payStatusMessage.put("8000", "正在处理中");
        payStatusMessage.put("4000", "订单支付失败");
        payStatusMessage.put("5000", "重复请求");
        payStatusMessage.put("6001", "用户中途取消");
        payStatusMessage.put("6002", "网络连接出错");
        payStatusMessage.put("6004", "支付结果未知");
    }

    public AliPayResult(Map<String, String> rawResult) {
        if (rawResult == null) {
            return;
        }

        for (String key : rawResult.keySet()) {
            if (TextUtils.equals(key, "resultStatus")) {
                resultStatus = rawResult.get(key);
            } else if (TextUtils.equals(key, "result")) {
                result = rawResult.get(key);
            } else if (TextUtils.equals(key, "memo")) {
                memo = rawResult.get(key);
            }
        }
    }

    @Override
    public String toString() {
        return "resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}";
    }

    /**
     * @return the resultStatus
     */
    public String getResultStatus() {
        return resultStatus;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    public String getMessage() {
        return payStatusMessage.get(resultStatus);
    }

    public boolean isCanceled() {
        return TextUtils.equals(resultStatus, "6001");
    }

    public boolean isSuccessed() {
        return TextUtils.equals(resultStatus, "9000");
    }

    public boolean isError() {
        return TextUtils.equals(resultStatus, "4000") || TextUtils.equals(resultStatus, "5000") || TextUtils.equals(resultStatus, "6002");
    }
}
