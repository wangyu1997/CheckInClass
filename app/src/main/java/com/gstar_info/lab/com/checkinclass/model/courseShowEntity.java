package com.gstar_info.lab.com.checkinclass.model;

import java.util.List;

/**
 * Created by wangyu on 03/04/2017.
 */

public class courseShowEntity {
    /**
     * error : false
     * msg : ok
     * data : [{"id":"3","c_name":"数据结构与算法","gpa":"3","tId":"58e21ecd3ed1d","teacher":"王宇","header":"http://onqif3xou.bkt.clouddn.com/Fr4l8AkpfB3B9weYeMrgJxm2SW6A","place":"厚学312","number":"0","time":"2017-04-01 12:00:00","state":"0"},{"id":"4","c_name":"数字逻辑设计","gpa":"3","tId":"58e21ecd3ed1d","teacher":"王宇","header":"http://onqif3xou.bkt.clouddn.com/Fr4l8AkpfB3B9weYeMrgJxm2SW6A","place":"厚学312","number":"0","time":"2017-04-01 12:00:00","state":"0"},{"id":"5","c_name":"汇编语言程序设计","gpa":"3","tId":"58e21ecd3ed1d","teacher":"王宇","header":"http://onqif3xou.bkt.clouddn.com/Fr4l8AkpfB3B9weYeMrgJxm2SW6A","place":"厚学312","number":"0","time":"2017-04-01 12:00:00","state":"0"},{"id":"6","c_name":"多媒体技术及应用","gpa":"3","tId":"58e21ecd3ed1d","teacher":"王宇","header":"http://onqif3xou.bkt.clouddn.com/Fr4l8AkpfB3B9weYeMrgJxm2SW6A","place":"厚学312","number":"0","time":"2017-04-01 12:00:00","state":"0"},{"id":"7","c_name":"马克思主义与中国特色社会主义概论","gpa":"3","tId":"58e21ecd3ed1d","teacher":"王宇","header":"http://onqif3xou.bkt.clouddn.com/Fr4l8AkpfB3B9weYeMrgJxm2SW6A","place":"厚学312","number":"0","time":"2017-04-01 12:00:00","state":"0"},{"id":"8","c_name":"大学体育","gpa":"1","tId":"58e21ecd3ed1d","teacher":"王宇","header":"http://onqif3xou.bkt.clouddn.com/Fr4l8AkpfB3B9weYeMrgJxm2SW6A","place":"厚学312","number":"0","time":"2017-04-01 12:00:00","state":"0"}]
     */

    private boolean error;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * c_name : 数据结构与算法
         * gpa : 3
         * tId : 58e21ecd3ed1d
         * teacher : 王宇
         * header : http://onqif3xou.bkt.clouddn.com/Fr4l8AkpfB3B9weYeMrgJxm2SW6A
         * place : 厚学312
         * number : 0
         * time : 2017-04-01 12:00:00
         * state : 0
         */

        private String id;
        private String c_name;
        private String gpa;
        private String tId;
        private String teacher;
        private String header;
        private String place;
        private String number;
        private String time;
        private String state;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getC_name() {
            return c_name;
        }

        public void setC_name(String c_name) {
            this.c_name = c_name;
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

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
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
    }
}
