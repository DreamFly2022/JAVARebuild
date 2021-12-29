package com.hef.myidsequence.domain;

/**
 * @Date 2021/12/29
 * @Author lifei
 */
public enum ResultStatusEnum {
    SUCCESS(0, "成功"),
    FAIL(1, "失败")
    ;

    private int status;
    private String message;

    ResultStatusEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
