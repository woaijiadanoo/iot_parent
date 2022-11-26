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

    private String productKey;

    private String deviceName;


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

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
