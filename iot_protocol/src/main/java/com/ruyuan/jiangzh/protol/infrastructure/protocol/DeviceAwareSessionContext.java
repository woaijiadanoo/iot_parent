package com.ruyuan.jiangzh.protol.infrastructure.protocol;

import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.vo.DeviceInfoVO;

import java.util.UUID;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public abstract class DeviceAwareSessionContext implements SessionContext{

    protected final UUID sessionId;

    private volatile DeviceId deviceId;

    private volatile DeviceInfoVO deviceInfo;

    public DeviceAwareSessionContext(UUID sessionId){
        this.sessionId = sessionId;
    }

    /*
        判断session是否处于链接状态
     */
    public boolean isConnected(){
        return deviceInfo != null;
    }

    public void setDeviceInfo(DeviceInfoVO deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    @Override
    public UUID getSessionId() {
        return sessionId;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public DeviceInfoVO getDeviceInfo() {
        return deviceInfo;
    }
}
