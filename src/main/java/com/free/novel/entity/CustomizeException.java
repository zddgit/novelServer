package com.free.novel.entity;

public class CustomizeException extends RuntimeException {
    private String msg;
    private int  error;

    @Override
    public String getMessage() {
        if(msg==null){
            return super.getMessage();
        }else {
            return msg;
        }
    }

    public CustomizeException(int error, String msg){
        this.msg = msg;
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}
