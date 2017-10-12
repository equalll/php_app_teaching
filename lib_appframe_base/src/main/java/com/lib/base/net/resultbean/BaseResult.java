package com.lib.base.net.resultbean;

import com.google.gson.Gson;

/**
 * 项目中自定义网络请求 POJO 类请继承此类
 */
public class BaseResult {


    /**
     * status : 0
     * message : 授权码失败
     * data : []
     */

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
