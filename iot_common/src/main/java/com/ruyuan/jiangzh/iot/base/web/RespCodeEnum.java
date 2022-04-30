package com.ruyuan.jiangzh.iot.base.web;

public enum RespCodeEnum {

    SUCCESS(200, "业务成功")
    , RESOURCE_NOT_EXISTS(404, "资源不存在")
    , PARAM_IS_NULL(404, "参数为空")
    , DELETE_FAILTURE(500, "删除失败")
    , PERMISSION_DENIED(403, "访问不允许")
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
