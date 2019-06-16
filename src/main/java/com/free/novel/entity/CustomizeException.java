package com.free.novel.entity;

public class CustomizeException extends RuntimeException {
    private String msg;
    private int  code;

    @Override
    public String getMessage() {
        if(msg==null){
            return super.getMessage();
        }else {
            return msg;
        }
    }

    public CustomizeException(int code, String msg){
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
