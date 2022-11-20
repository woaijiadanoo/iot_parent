package com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt;

import com.ruyuan.jiangzh.iot.actors.msg.device.ServiceToDeviceAttributeMsg;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public interface SessionMsgListener {

    void onAttributeUpdate(ServiceToDeviceAttributeMsg deviceAttributeMsg);

}
