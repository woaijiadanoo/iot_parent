package com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt;

import com.ruyuan.jiangzh.protol.infrastructure.protocol.DeviceAwareSessionContext;

import java.util.UUID;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public abstract class MqttDeviceAwareSessionContext extends DeviceAwareSessionContext {

    public MqttDeviceAwareSessionContext(UUID sessionId){
        super(sessionId);
    }

}
