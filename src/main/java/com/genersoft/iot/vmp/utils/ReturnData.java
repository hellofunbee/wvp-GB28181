package com.genersoft.iot.vmp.utils;

import net.sf.json.JSONObject;

public class ReturnData {
    private String msg;
    private String code="01";
    private Object data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ReturnData() {
        this.msg = "";
    }

    public String toString() {
        JSONObject json = JSONObject.fromObject(this);
        return json.toString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
