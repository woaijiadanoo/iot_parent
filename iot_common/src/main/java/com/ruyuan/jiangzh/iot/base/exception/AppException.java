package com.ruyuan.jiangzh.iot.base.exception;

import com.ruyuan.jiangzh.iot.base.message.MessageHelper;

public class AppException extends LogException{

    // 统一异常返回相关的内容
    private  int code;
    private String state;
    /**
     *  目标：用来进行国际化填入的参数
     *  用户名不能为空
     *  用户名{0}不合法,合法的格式为{1}
     *  例子：用户名 天涯 不合法，合法的格式为六位数字
     *
     */
    private Object[] params;

    public AppException(int code,String msgId){
        super(msgId);
        this.code = code;
    }

    public AppException(int code,String state,String msgId, Object[] params){
        this(code,msgId);
        this.params = params;
    }

    public AppException(int code,String state,String msgId){
        super(msgId);
        this.code = code;
    }

    public AppException(int code,String msgId, Object[] params){
        this(code,msgId);
        this.params = params;
    }

    /**
     *  获取异常的具体信息
     * @return
     */
    @Override
    public String getMessage(){
        // 如果没有params
        if(params == null || params.length == 0){
            return super.getMessage();
        }else{
            return MessageHelper.getMessage(getMsgId(),params);
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
