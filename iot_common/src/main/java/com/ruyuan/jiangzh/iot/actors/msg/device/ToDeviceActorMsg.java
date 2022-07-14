package com.ruyuan.jiangzh.iot.actors.msg.device;

import com.ruyuan.jiangzh.iot.actors.msg.BaseMessage;
import com.ruyuan.jiangzh.iot.actors.msg.ServerAddress;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

import java.io.Serializable;
import java.util.Optional;

public interface ToDeviceActorMsg extends BaseMessage {

    TenantId getTenantId();
    DeviceId getDeviceId();

    FromDeviceMsg getPayload();

    Optional<ServerAddress> getServerAddress();

}
