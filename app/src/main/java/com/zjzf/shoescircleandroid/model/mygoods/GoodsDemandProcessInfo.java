package com.zjzf.shoescircleandroid.model.mygoods;

import com.zjzf.shoescircle.lib.base.baseadapter.MultiType;

/**
 * Created by 陈志远 on 2018/11/24.
 * 我的求货-求货中
 */
public class GoodsDemandProcessInfo implements MultiType {

    /**
     * id : 1015889815853224000
     * enable : 1
     * createTime : 2018-07-08 09:29:37
     * memId : 1
     * member : {"id":1,"enable":1,"createTime":"2018-07-08 08:46:54","account":"hehaoyang","phone":"15857193035","memName":"hehaoyang666","avatar":"","sex":0,"evaluate":5,"score":0,"level":1}
     * name : 乔丹7
     * photo : tfhf23121312
     * freightNo : AA223AA5
     * size : 41,42
     * price : 66
     * state : 1
     */

    private String id;
    private int enable;
    private String createTime;
    private int memId;
    private MemberBean member;
    private String name;
    private String photo;
    private String freightNo;
    private String size;
    private int price;
    private int state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public int getMemId() {
        return memId;
    }

    public void setMemId(int memId) {
        this.memId = memId;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
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

    @Override
    public int getItemType() {
        return 0;
    }

    public static class MemberBean {
        /**
         * id : 1
         * enable : 1
         * createTime : 2018-07-08 08:46:54
         * account : hehaoyang
         * phone : 15857193035
         * memName : hehaoyang666
         * avatar :
         * sex : 0
         * evaluate : 5
         * score : 0
         * level : 1
         */

        private int id;
        private int enable;
        private String createTime;
        private String account;
        private String phone;
        private String memName;
        private String avatar;
        private int sex;
        private int evaluate;
        private int score;
        private int level;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMemName() {
            return memName;
        }

        public void setMemName(String memName) {
            this.memName = memName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getEvaluate() {
            return evaluate;
        }

        public void setEvaluate(int evaluate) {
            this.evaluate = evaluate;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}
