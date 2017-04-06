package com.gstar_info.lab.com.checkinclass.model;

/**
 * Created by ritchiehuang on 4/6/17.
 */

public class CourseinfoEntity {

    /**
     * error : false
     * msg : ok
     * data : {"id":"1","cid":"1","c_name":"数据结构与算法","aid":"1","a_name":"计算机科学与技术学院","gpa":"3","tId":"58e37c3e02392","wifi":"暂未指定","signFlag":"2","teacher":"王宇","header":"http://onqif3xou.bkt.clouddn.com/FibvckDcumTOTMwXytEiJlHODHvl","header_con":"http://onqif3xou.bkt.clouddn.com/FlG45A7c4KSV6qUoKe7R10V85ilS;","place":"厚学312","content":"请同学们准时到","number":"1","time":"第八周 周二 1/2","state":"0","createTime":"2017-04-06 00:03:09"}
     */

    private boolean error;
    private String msg;
    private DataBean data;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * cid : 1
         * c_name : 数据结构与算法
         * aid : 1
         * a_name : 计算机科学与技术学院
         * gpa : 3
         * tId : 58e37c3e02392
         * wifi : 暂未指定
         * signFlag : 2
         * teacher : 王宇
         * header : http://onqif3xou.bkt.clouddn.com/FibvckDcumTOTMwXytEiJlHODHvl
         * header_con : http://onqif3xou.bkt.clouddn.com/FlG45A7c4KSV6qUoKe7R10V85ilS;
         * place : 厚学312
         * content : 请同学们准时到
         * number : 1
         * time : 第八周 周二 1/2
         * state : 0
         * createTime : 2017-04-06 00:03:09
         */

        private String id;
        private String cid;
        private String c_name;
        private String aid;
        private String a_name;
        private String gpa;
        private String tId;
        private String wifi;
        private String signFlag;
        private String teacher;
        private String header;
        private String header_con;
        private String place;
        private String content;
        private String number;
        private String time;
        private String state;
        private String createTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getC_name() {
            return c_name;
        }

        public void setC_name(String c_name) {
            this.c_name = c_name;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getA_name() {
            return a_name;
        }

        public void setA_name(String a_name) {
            this.a_name = a_name;
        }

        public String getGpa() {
            return gpa;
        }

        public void setGpa(String gpa) {
            this.gpa = gpa;
        }

        public String getTId() {
            return tId;
        }

        public void setTId(String tId) {
            this.tId = tId;
        }

        public String getWifi() {
            return wifi;
        }

        public void setWifi(String wifi) {
            this.wifi = wifi;
        }

        public String getSignFlag() {
            return signFlag;
        }

        public void setSignFlag(String signFlag) {
            this.signFlag = signFlag;
        }

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getHeader_con() {
            return header_con;
        }

        public void setHeader_con(String header_con) {
            this.header_con = header_con;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
