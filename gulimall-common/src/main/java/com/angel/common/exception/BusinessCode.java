package com.angel.common.exception;

public enum  BusinessCode {

    OTHER_EXCEPTION(500,"其它异常"),
    VALIDATE_FAILED(400,"数据校验错误");

    private int code;
    private String errorMsg;

    BusinessCode(int code,String errorMsg){
        this.code = code;
        this.errorMsg = errorMsg;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "BusinessCode{" +
                "code=" + code +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
