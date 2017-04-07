package com.gstar_info.lab.com.checkinclass.model;

import java.util.List;

/**
 * Created by ritchiehuang on 4/7/17.
 */

public class SignHistoryEntity {


    /**
     * error : false
     * msg : ok
     * data : [{"id":"2","sid":"58e3a5177f994","sname":"王宇","header":null,"signState":"2"}]
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
         * id : 2
         * sid : 58e3a5177f994
         * sname : 王宇
         * header : null
         * signState : 2
         */

        private String id;
        private String sid;
        private String sname;
        private Object header;
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

        public Object getHeader() {
            return header;
        }

        public void setHeader(Object header) {
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
