package com.ruyuan.jiangzh.iot.base.web;

public enum RespCodeEnum {

    SUCCESS(200, "业务成功")
    , RESOURCE_NOT_EXISTS(404, "资源不存在")
    , SYSTEM_ERROR(500, "系统未知异常")
    ;

    private int code;
    private String message;

    RespCodeEnum(int code,String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
