package com.ruyuan.jiangzh.service.dto;

import java.io.Serializable;

public class DeviceSercetDTO implements Serializable {

    private String deviceId;
    private String productId;

    private String tenantId;

    private String userId;

    // 三元组信息
    private String productKey;
    private String deviceName;
    private String deviceSercet;
    // 其他相关信息
    private String productSecret;
    private Boolean autoActive;
    private Integer deviceStatus;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getDeviceSercet() {
        return deviceSercet;
    }

    public void setDeviceSercet(String deviceSercet) {
        this.deviceSercet = deviceSercet;
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

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "DeviceSercetDTO{" +
                "deviceId='" + deviceId + '\'' +
                ", productId='" + productId + '\'' +
                ", productKey='" + productKey + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceSercet='" + deviceSercet + '\'' +
                ", productSecret='" + productSecret + '\'' +
                ", autoActive=" + autoActive +
                ", deviceStatus=" + deviceStatus +
                '}';
    }
}
