package com.ruyuan.jiangzh.iot.actors.msg.device;

import com.ruyuan.jiangzh.iot.actors.msg.ServerAddress;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

import java.util.Optional;

public interface ToDeviceActorMsg {

    TenantId getTenantId();
    DeviceId getDeviceId();

    FromDeviceMsg getPayload();

    Optional<ServerAddress> getServerAddress();

}
