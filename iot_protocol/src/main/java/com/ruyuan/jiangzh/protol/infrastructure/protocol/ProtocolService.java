package com.ruyuan.jiangzh.protol.infrastructure.protocol;

import com.ruyuan.jiangzh.iot.actors.msg.device.FromDeviceMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.common.ProtocolServiceCallback;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthRespMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.vo.DeviceInfoVO;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public interface ProtocolService {

    // 协议的处理流
    void process(DeviceAuthReqMsg msg, ProtocolServiceCallback<DeviceAuthRespMsg> callback);

    void process(FromDeviceMsg msg);

    boolean checkLimits(DeviceInfoVO deviceInfo, Object msg, ProtocolServiceCallback<DeviceAuthRespMsg> callback);

}
