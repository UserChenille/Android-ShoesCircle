package com.zjzf.shoescircleandroid.model;

/**
 * Created by 陈志远 on 2019/3/13.
 */
public class PopupCommentInfo {
    private String avatar;
    private String name;
    private float rating;
    private String createTime;
    private String tradeNum;

    public PopupCommentInfo() {
    }

    public static PopupCommentInfo createFromGoodsDetailInfo(GoodsDetailInfo goodsDetailInfo) {
        PopupCommentInfo info = new PopupCommentInfo();
        if (goodsDetailInfo != null && goodsDetailInfo.getMember() != null) {
            info.setAvatar(goodsDetailInfo.getMember().getAvatar());
            info.setName(goodsDetailInfo.getMember().getMemName());
            info.setRating(goodsDetailInfo.getMember().getEvaluate());
            info.setCreateTime(goodsDetailInfo.getMember().getCreateTime());
            info.setTradeNum(goodsDetailInfo.getMember().getTradeNum());
        }
        return info;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public PopupCommentInfo setName(String name) {
        this.name = name;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public PopupCommentInfo setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public String getCreateTime() {
        return createTime == null ? "" : createTime;
    }

    public PopupCommentInfo setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getTradeNum() {
        return tradeNum == null ? "" : tradeNum;
    }

    public PopupCommentInfo setTradeNum(String tradeNum) {
        this.tradeNum = tradeNum;
        return this;
    }

    public String getAvatar() {
        return avatar == null ? "" : avatar;
    }

    public PopupCommentInfo setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }
}
