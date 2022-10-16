package com.ruyuan.jiangzh.protol.infrastructure.protocol.messages;

import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthRespMsg;

/**
 * @author jiangzheng
 * @version 1.0
 * @description: 协议返回的消息封装
 */
public class ProtocolApiRespMsg {

    private DeviceAuthRespMsg deviceAuthRespMsg;

    public ProtocolApiRespMsg(DeviceAuthRespMsg deviceAuthRespMsg){
        this.deviceAuthRespMsg = deviceAuthRespMsg;
    }

    public DeviceAuthRespMsg getDeviceAuthRespMsg() {
        return deviceAuthRespMsg;
    }

}
