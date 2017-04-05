package com.gstar_info.lab.com.checkinclass.model;

/**
 * Created by wangyu on 03/04/2017.
 */

public class ObjEntity {
    /**
     * error : false
     * msg : ok
     * data : {}
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
    }
}
