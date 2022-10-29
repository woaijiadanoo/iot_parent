package com.ruyuan.jiangzh.protol.infrastructure.protocol.vo;

import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.device.ProductId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class DeviceInfoVO {

    private DeviceId deviceId;

    private TenantId tenantId;

    private ProductId productId;

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }

    public ProductId getProductId() {
        return productId;
    }

    public void setProductId(ProductId productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "DeviceInfoVO{" +
                "deviceId=" + deviceId +
                ", tenantId=" + tenantId +
                ", productId=" + productId +
                '}';
    }
}
