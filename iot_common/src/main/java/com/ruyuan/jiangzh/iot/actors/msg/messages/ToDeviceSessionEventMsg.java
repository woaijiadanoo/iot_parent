package com.ruyuan.jiangzh.iot.actors.msg.messages;

import com.ruyuan.jiangzh.iot.actors.msg.MsgType;
import com.ruyuan.jiangzh.iot.actors.msg.ServerAddress;
import com.ruyuan.jiangzh.iot.actors.msg.ToAllNodesMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.FromDeviceMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.ToDeviceActorMsg;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

import java.util.Optional;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class ToDeviceSessionEventMsg implements ToAllNodesMsg, ToDeviceActorMsg {

    private final DeviceId deviceId;
    private final TenantId tenantId;
    /*
        0-OPEN  1-CLOSE
     */
    private final int sessionEventCode;


    public ToDeviceSessionEventMsg(DeviceId deviceId,TenantId tenantId,int sessionEventCode){
        this.deviceId = deviceId;
        this.tenantId = tenantId;
        this.sessionEventCode = sessionEventCode;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TO_DEVICE_SESSION_EVENT_MSG;
    }

    @Override
    public TenantId getTenantId() {
        return null;
    }

    @Override
    public DeviceId getDeviceId() {
        return null;
    }

    @Override
    public FromDeviceMsg getPayload() {
        return null;
    }

    @Override
    public Optional<ServerAddress> getServerAddress() {
        return Optional.empty();
    }
}
