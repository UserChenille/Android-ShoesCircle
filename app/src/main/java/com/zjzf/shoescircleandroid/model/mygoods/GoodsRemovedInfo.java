package com.zjzf.shoescircleandroid.model.mygoods;

import com.zjzf.shoescircle.lib.base.baseadapter.MultiType;

/**
 * Created by 陈志远 on 2019/3/9.
 */
public class GoodsRemovedInfo implements MultiType {

    /**
     * id : 1052488087349997600
     * enable : 1
     * createTime : 2018-10-17 17:14:49
     * memId : 1052218251554304000
     * member : null
     * name : 测试1
     * photo : https://b-ssl.duitang.com/uploads/item/201404/07/20140407112021_fx5LT.thumb.700_0.jpeg
     * freightNo : aa123
     * size : 39
     * price : 20
     * state : 0
     */

    private long id;
    private int enable;
    private String createTime;
    private long memId;
    private String member;
    private String name;
    private String photo;
    private String freightNo;
    private String size;
    private int price;
    private int state;

    @Override
    public int getItemType() {
        return 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getMemId() {
        return memId;
    }

    public void setMemId(long memId) {
        this.memId = memId;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFreightNo() {
        return freightNo;
    }

    public void setFreightNo(String freightNo) {
        this.freightNo = freightNo;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
