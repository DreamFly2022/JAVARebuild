package com.hef.myidsequence.domain;

/**
 * @Date 2021/12/29
 * @Author lifei
 */
public class ResponseResult<T> {

    private T result;
    private int status;
    private String message;

    public ResponseResult() {
    }

    public ResponseResult(T result, ResultStatusEnum resultStatusEnum) {
        this.result = result;
        this.status = resultStatusEnum.getStatus();
        this.message = resultStatusEnum.getMessage();
    }

    public ResponseResult(T result, int status, String message) {
        this.result = result;
        this.status = status;
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
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
