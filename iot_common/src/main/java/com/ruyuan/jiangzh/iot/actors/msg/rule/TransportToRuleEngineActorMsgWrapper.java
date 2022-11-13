package com.ruyuan.jiangzh.iot.actors.msg.rule;

import com.ruyuan.jiangzh.iot.actors.msg.MsgType;
import com.ruyuan.jiangzh.iot.actors.msg.ServerAddress;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

import java.util.Optional;

public class TransportToRuleEngineActorMsgWrapper implements ToRuleEngineActorMsg{

    private ServerAddress serverAddress;

    private final TenantId tenantId;

    private final DeviceId deviceId;

    private TransportToRuleEngineActorMsg msg;

    public TransportToRuleEngineActorMsgWrapper(
            TenantId tenantId, DeviceId deviceId,
            TransportToRuleEngineActorMsg msg) {
        this.tenantId = tenantId;
        this.deviceId = deviceId;
        this.msg = msg;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TRANSPORT_TO_RULE_ENGINE_ACTOR_MSG_WRAPPER;
    }

    @Override
    public TenantId getTenantId() {
        return tenantId;
    }

    @Override
    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setServerAddress(ServerAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    public TransportToRuleEngineActorMsg getMsg() {
        return msg;
    }

    @Override
    public Optional<ServerAddress> getServerAddress() {
        if(serverAddress != null){
            return Optional.of(serverAddress);
        }else{
            return Optional.empty();
        }
    }
}
