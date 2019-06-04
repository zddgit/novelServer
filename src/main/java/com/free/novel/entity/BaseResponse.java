package com.free.novel.entity;

public class BaseResponse<T> {
    private int error;
    private String message;
    private T data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseResponse(int error, String message, T data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(int error, String message) {
        this.error = error;
        this.message = message;
    }


}
