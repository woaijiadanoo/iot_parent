package com.ruyuan.jiangzh.protol.infrastructure.protocol.vo;

import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

import java.util.UUID;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class SessionInfoVO {

    private final UUID sessionId;

    private final DeviceId deviceId;

    private final TenantId tenantId;

    public SessionInfoVO(UUID sessionId,DeviceId deviceId,TenantId tenantId){
        this.sessionId = sessionId;
        this.deviceId = deviceId;
        this.tenantId = tenantId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public TenantId getTenantId() {
        return tenantId;
    }
}
