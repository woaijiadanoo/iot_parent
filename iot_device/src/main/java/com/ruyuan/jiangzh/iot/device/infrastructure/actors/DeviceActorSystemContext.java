package com.ruyuan.jiangzh.iot.device.infrastructure.actors;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.factory.AggrDeviceFactory;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class DeviceActorSystemContext extends ActorSystemContext {

    private final AggrDeviceFactory deviceFactory;

    public DeviceActorSystemContext(AggrDeviceFactory deviceFactory){
        this.deviceFactory = deviceFactory;
    }

    public AggrDeviceFactory getDeviceFactory() {
        return deviceFactory;
    }
}
