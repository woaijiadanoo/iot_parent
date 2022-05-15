package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity;

public class AggrDeviceSercetEntity {

    private String productKey;
    private String deviceSecret;
    private String deviceName;
    private String productSecret;
    private Boolean autoActive;

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
