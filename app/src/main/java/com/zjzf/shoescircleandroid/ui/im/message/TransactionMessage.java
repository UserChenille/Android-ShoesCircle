package com.zjzf.shoescircleandroid.ui.im.message;

import android.os.Parcel;

import com.zjzf.shoescircle.lib.tools.gson.GsonUtil;
import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircleandroid.model.im.TransactionMessageContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.push.common.ParcelUtils;

/**
 * Created by 陈志远 on 2018/12/9.
 */
@MessageTag(value = "XQ:TradeMsg", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class TransactionMessage extends MessageContent {
    private TransactionMessageContent content;

    public TransactionMessage(TransactionMessageContent content) {
        this.content = content;
        LogHelper.trace("交易信息", GsonUtil.INSTANCE.toString(content));
    }

    public TransactionMessage(byte[] data) {
        super(data);
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
            LogHelper.trace("交易信息", jsonStr);
        } catch (UnsupportedEncodingException e1) {

        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("content"))
                content = GsonUtil.INSTANCE.toObject(jsonObj.optString("content"), TransactionMessageContent.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected TransactionMessage(Parcel in) {
        content = ParcelUtils.readFromParcel(in, TransactionMessageContent.class);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionMessage> CREATOR = new Creator<TransactionMessage>() {
        @Override
        public TransactionMessage createFromParcel(Parcel in) {
            return new TransactionMessage(in);
        }

        @Override
        public TransactionMessage[] newArray(int size) {
            return new TransactionMessage[size];
        }
    };

    public TransactionMessageContent getContent() {
        return content;
    }

    public TransactionMessage setContent(TransactionMessageContent content) {
        this.content = content;
        return this;
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("content", GsonUtil.INSTANCE.toString(content));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "TransactionMessage:\n" + GsonUtil.INSTANCE.toString(content) + "\n";
    }
}
