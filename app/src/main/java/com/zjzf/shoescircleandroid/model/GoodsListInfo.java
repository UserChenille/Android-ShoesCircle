package com.zjzf.shoescircleandroid.model;

import com.zjzf.shoescircle.lib.base.baseadapter.MultiType;

import java.util.List;


/**
 * Created by 陈志远 on 2018/8/5.
 */
public class GoodsListInfo {

    /**
     * offset : 0
     * limit : 2147483647
     * total : 8
     * size : 1
     * pages : 8
     * current : 1
     * searchCount : true
     * openSort : true
     * orderByField : id
     * records : [{"id":1015889815853224000,"enable":1,"createTime":"2018-07-08 09:29:37","memId":1,"member":{"id":1,"enable":1,"createTime":"2018-07-08 08:46:54","account":"hehaoyang","phone":"15857193035","memName":"何昊阳","avatar":"","sex":0,"evaluate":5,"score":0,"level":1},"name":"乔丹6","photo":"tfhf23121312","freightNo":"AA223AA5","size":"41,42","price":111,"state":1}]
     * condition : null
     * asc : true
     * offsetCurrent : 0
     */

    private int offset;
    private int limit;
    private int total;
    private int size;
    private int pages;
    private int current;
    private boolean searchCount;
    private boolean openSort;
    private String orderByField;
    private Object condition;
    private boolean asc;
    private int offsetCurrent;
    private List<RecordsBean> records;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public boolean isOpenSort() {
        return openSort;
    }

    public void setOpenSort(boolean openSort) {
        this.openSort = openSort;
    }

    public String getOrderByField() {
        return orderByField;
    }

    public void setOrderByField(String orderByField) {
        this.orderByField = orderByField;
    }

    public Object getCondition() {
        return condition;
    }

    public void setCondition(Object condition) {
        this.condition = condition;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public int getOffsetCurrent() {
        return offsetCurrent;
    }

    public void setOffsetCurrent(int offsetCurrent) {
        this.offsetCurrent = offsetCurrent;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean implements MultiType {
        /**
         * id : 1015889815853224000
         * enable : 1
         * createTime : 2018-07-08 09:29:37
         * memId : 1
         * member : {"id":1,"enable":1,"createTime":"2018-07-08 08:46:54","account":"hehaoyang","phone":"15857193035","memName":"何昊阳","avatar":"","sex":0,"evaluate":5,"score":0,"level":1}
         * name : 乔丹6
         * photo : tfhf23121312
         * freightNo : AA223AA5
         * size : 41,42
         * price : 111
         * state : 1
         */

        private long id;
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
             * memName : 何昊阳
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
}
