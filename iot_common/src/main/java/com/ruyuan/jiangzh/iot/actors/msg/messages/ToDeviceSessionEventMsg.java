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

    private ServerAddress serverAddress;

    private long lastActivityTimestamp;

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
        return tenantId;
    }

    @Override
    public DeviceId getDeviceId() {
        return deviceId;
    }

    @Override
    public FromDeviceMsg getPayload() {
        return null;
    }

    public void setServerAddress(ServerAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getSessionEventCode() {
        return sessionEventCode;
    }

    @Override
    public Optional<ServerAddress> getServerAddress() {
        if(serverAddress != null){
            return Optional.of(serverAddress);
        }
        return Optional.empty();
    }

    public long getLastActivityTimestamp() {
        return lastActivityTimestamp;
    }

    public void setLastActivityTimestamp(long lastActivityTimestamp) {
        this.lastActivityTimestamp = lastActivityTimestamp;
    }

    @Override
    public String toString() {
        return "ToDeviceSessionEventMsg{" +
                "deviceId=" + deviceId +
                ", tenantId=" + tenantId +
                ", sessionEventCode=" + sessionEventCode +
                ", serverAddress=" + serverAddress +
                '}';
    }
}
