package com.cloud.mina.bean;

public class Message {
//    操作状态码
    private int code ;
//    操作后提示信息
    private String message;
//    操作返回数据
    private Object data ;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
