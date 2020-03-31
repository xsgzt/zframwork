package com.ztyb.framework.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/19.
 * {
 * ║
 * ║         "data": "123456"
 * ║
 * ║
 * ║         "resultMessage": "成功",
 * ║         "statusCode": 1000
 * ║
 * ║ }-
 */

public class BaseResult implements Serializable {
    public int code;
    public String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }





    public boolean isOk() {
        return "200".equals(code);
    }

}
