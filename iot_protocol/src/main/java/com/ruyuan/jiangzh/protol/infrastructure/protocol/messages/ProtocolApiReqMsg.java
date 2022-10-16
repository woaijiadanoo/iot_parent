package com.ruyuan.jiangzh.protol.infrastructure.protocol.messages;

import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthReqMsg;

/**
 * @author jiangzheng
 * @version 1.0
 * @description: 协议请求的消息
 */
public class ProtocolApiReqMsg {

    private DeviceAuthReqMsg deviceAuthReqMsg;

    public ProtocolApiReqMsg(DeviceAuthReqMsg deviceAuthReqMsg){
        this.deviceAuthReqMsg = deviceAuthReqMsg;
    }


    public DeviceAuthReqMsg getDeviceAuthReqMsg() {
        return deviceAuthReqMsg;
    }

    public void setDeviceAuthReqMsg(DeviceAuthReqMsg deviceAuthReqMsg) {
        this.deviceAuthReqMsg = deviceAuthReqMsg;
    }
}
