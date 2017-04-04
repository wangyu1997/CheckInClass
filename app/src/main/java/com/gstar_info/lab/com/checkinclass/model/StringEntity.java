package com.gstar_info.lab.com.checkinclass.model;

/**
 * Created by wangyu on 03/04/2017.
 */

public class StringEntity {

    /**
     * error : false
     * msg : ok
     * data : Wd#jgFXO@)
     */

    private boolean error;
    private String msg;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
