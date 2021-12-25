package com.hef.springkafkademo.domain;

/**
 * @Date 2021/12/25
 * @Author lifei
 */
public class ResResult<T> {

    private T data;
    private int status;
    private String message;

    public ResResult() {
    }

    public ResResult(T data, int status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

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
}
