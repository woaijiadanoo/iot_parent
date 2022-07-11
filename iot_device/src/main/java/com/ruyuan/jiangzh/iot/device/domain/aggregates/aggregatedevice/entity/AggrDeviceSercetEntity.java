package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity;

import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceSercetInfoPO;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;

public class AggrDeviceSercetEntity {

    private String productKey;
    private String deviceSecret;
    private String deviceName;
    private String productSecret;
    private Boolean autoActive;


    public void poToEntity(DeviceSercetInfoPO po){
        this.setProductKey(po.getProductKey());
        this.setDeviceSecret(po.getDeviceSecret());
        this.setDeviceName(po.getDeviceName());
        this.setProductSecret(po.getProductSecret());
        this.setAutoActive(po.getAutoActive());
    }

    public DeviceSercetInfoPO entityToPO(DeviceId deviceId){
        DeviceSercetInfoPO deviceSercetInfoPO = new DeviceSercetInfoPO();
        deviceSercetInfoPO.setUuid(UUIDHelper.fromTimeUUID(deviceId.getUuid()));
        deviceSercetInfoPO.setDeviceName(this.getDeviceName());
        deviceSercetInfoPO.setProductKey(this.getProductKey());
        deviceSercetInfoPO.setProductSecret(this.getProductSecret());
        deviceSercetInfoPO.setDeviceSecret(this.getDeviceSecret());
        deviceSercetInfoPO.setAutoActive(this.getAutoActive());

        return deviceSercetInfoPO;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public void setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getProductSecret() {
        return productSecret;
    }

    public void setProductSecret(String productSecret) {
        this.productSecret = productSecret;
    }

    public Boolean getAutoActive() {
        return autoActive;
    }

    public void setAutoActive(Boolean autoActive) {
        this.autoActive = autoActive;
    }

    @Override
    public String toString() {
        return "AggrDeviceSercetEntity{" +
                "productKey='" + productKey + '\'' +
                ", deviceSecret='" + deviceSecret + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", productSecret='" + productSecret + '\'' +
                ", autoActive=" + autoActive +
                '}';
    }
}
