package com.gstar_info.lab.com.checkinclass.model;

import java.util.List;

/**
 * Created by ritchiehuang on 4/7/17.
 */

public class SignHistoryEntity {
    /**
     * error : false
     * msg : ok
     * data : [{"id":"1","sid":"58e4fe199b42d","sname":"人不如故","mname":"计算机科学与技术","header":"http://www.baidu.com","signState":"2"}]
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
         * id : 1
         * sid : 58e4fe199b42d
         * sname : 人不如故
         * mname : 计算机科学与技术
         * header : http://www.baidu.com
         * signState : 2
         */

        private String id;
        private String sid;
        private String sname;
        private String mname;
        private String header;
        private String signState;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getMname() {
            return mname;
        }

        public void setMname(String mname) {
            this.mname = mname;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getSignState() {
            return signState;
        }

        public void setSignState(String signState) {
            this.signState = signState;
        }
    }
}
