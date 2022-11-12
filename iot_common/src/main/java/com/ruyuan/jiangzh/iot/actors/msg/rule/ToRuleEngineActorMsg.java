package com.ruyuan.jiangzh.iot.actors.msg.rule;

import com.ruyuan.jiangzh.iot.actors.msg.MsgType;
import com.ruyuan.jiangzh.iot.actors.msg.ServerAddress;
import com.ruyuan.jiangzh.iot.actors.msg.device.FromDeviceMsg;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

import java.util.Optional;

public interface ToRuleEngineActorMsg extends FromDeviceMsg {

    MsgType getMsgType();

    TenantId getTenantId();

    DeviceId getDeviceId();



    Optional<ServerAddress> getServerAddress();

}
