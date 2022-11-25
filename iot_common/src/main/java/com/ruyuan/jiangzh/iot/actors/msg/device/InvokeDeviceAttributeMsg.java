package com.ruyuan.jiangzh.iot.actors.msg.device;

import com.ruyuan.jiangzh.iot.actors.msg.MsgType;
import com.ruyuan.jiangzh.iot.actors.msg.ServerAddress;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

public class InvokeDeviceAttributeMsg implements ToDeviceMsg{

    private TenantId tenantId;

    private DeviceId deviceId;

    private ServerAddress serverAddress;

    private String sessionId;

    private String msg;

    @Override
    public MsgType getMsgType() {
        return MsgType.INVOKE_DEVICE_ATTR_MSG;
    }

    @Override
    public boolean broadcast() {
        return true;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public ServerAddress getServerAddress() {
        return serverAddress;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public void setServerAddress(ServerAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "InvokeDeviceAttributeMsg{" +
                "tenantId=" + tenantId +
                ", deviceId=" + deviceId +
                ", serverAddress=" + serverAddress +
                ", sessionId='" + sessionId + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
