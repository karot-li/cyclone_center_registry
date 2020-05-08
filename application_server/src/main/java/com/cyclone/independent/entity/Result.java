package com.cyclone.independent.entity;

public class Result {

    public static String SUCCESS_CODE = "2000";
    public static String ERROR_CODE = "5000";

    boolean flag;
    String message;
    String code;

    public Result(boolean flag, String message, String code) {
        this.flag = flag;
        this.message = message;
        this.code = code;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
