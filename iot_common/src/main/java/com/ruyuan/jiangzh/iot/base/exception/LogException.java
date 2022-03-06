package com.ruyuan.jiangzh.iot.base.exception;

import com.ruyuan.jiangzh.iot.base.message.MessageHelper;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;

public class LogException extends RuntimeException {

    // 请求唯一标识
    private String requestId;

    // 异常信息编号
    private String msgId;

    // 将要被记录的异常信息JSON字符串
    private String exMessage;

    // 出现的异常
    private Throwable exception;

    /**
     *  msgId - 国际化错误的唯一标识
     *      中文 - [  device.user.isnotnull - 用户名不能为空 ]
     *      英文 - [  device.user - username : allen 不合法 ]
     * @param msgId
     */
    public LogException(String msgId){
        this(msgId,"",null);
    }

    public LogException(String msgId, String exMessage){
        this(msgId,exMessage,null);
    }

    public LogException(String msgId, String exMessage, Throwable exception){
        this.requestId = IoTStringUtils.requestId();
        this.msgId = msgId;
        this.exMessage = exMessage;
        this.exception = exception;
    }

    /**
     *  获取异常的具体信息
     * @return
     */
    @Override
    public String getMessage(){
        return MessageHelper.getMessage(msgId);
    }

    public String getRequestId() {
        return requestId;
    }

    public String getMsgId() {
        return msgId;
    }

    public String getExMessage() {
        return exMessage;
    }

    public Throwable getException() {
        return exception;
    }

}
