package com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth;

import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.device.ProductId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.UserId;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.service.dto.DeviceSercetDTO;

/**
 * @author jiangzheng
 * @version 1.0
 * @description: 鉴权相应的消息体
 */
public class DeviceAuthRespMsg {

    private DeviceId deviceId;

    private TenantId tenantId;

    private UserId userId;

    private ProductId productId;

    private String deviceName;

    public static DeviceAuthRespMsg getRespByDeviceDTO(DeviceSercetDTO dto){
        DeviceAuthRespMsg respMsg = new DeviceAuthRespMsg();
        respMsg.setDeviceId(new DeviceId(IoTStringUtils.toUUID(dto.getDeviceId())));
        // tenantId和UserId
        respMsg.setTenantId(new TenantId(IoTStringUtils.toUUID(dto.getTenantId())));
        respMsg.setUserId(new UserId(IoTStringUtils.toUUID(dto.getUserId())));
        respMsg.setProductId(new ProductId(IoTStringUtils.toUUID(dto.getProductId())));

        respMsg.setDeviceName(dto.getDeviceName());

        return respMsg;
    }


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

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public ProductId getProductId() {
        return productId;
    }

    public void setProductId(ProductId productId) {
        this.productId = productId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
