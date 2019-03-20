package com.zjzf.shoescircleandroid.model.express;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 陈志远 on 2019/3/11.
 */
public class ExpressInfo {

    /**
     * checkpoint_status : delivered
     * Details :
     * StatusDescription : 快件已签收 签收人: 邮件收发章 感谢使用圆通速递，期待再次为您服务
     * Date : 2018-09-04 10:06:12
     */

    @SerializedName("checkpoint_status")
    private String mCheckpointStatus;
    @SerializedName("Details")
    private String mDetails;
    @SerializedName("StatusDescription")
    private String mStatusDescription;
    @SerializedName("Date")
    private String mDate;

    public String getCheckpointStatus() {
        return mCheckpointStatus;
    }

    public void setCheckpointStatus(String checkpointStatus) {
        mCheckpointStatus = checkpointStatus;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String details) {
        mDetails = details;
    }

    public String getStatusDescription() {
        return mStatusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        mStatusDescription = statusDescription;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }
}
