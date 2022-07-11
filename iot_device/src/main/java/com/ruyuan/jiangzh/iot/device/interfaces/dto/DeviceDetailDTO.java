package com.ruyuan.jiangzh.iot.device.interfaces.dto;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceEntity;

public class DeviceDetailDTO {

    private String deviceId;
    private String deviceName;
    private String cnName;
    // 产品信息
    private String productId;

    private String productName;


    private Integer deviceType;

    private String region;

    // 认证方式: secretKey
    private String authType;

    // IP地址
    private String ipAddr;
    // 固件版本
    private String fwVersion;
    // 激活时间
    private Long activeTime;
    // 最后在线时间
    private Long lastOnlineTime;
    // 设备状态
    private Integer deviceStatus;

    private String sdkType;
    private String sdkVersion;

    private String productKey;
    private String deviceSecret;
    private String productSecret;
    private Boolean autoActive;

    /*
        deviceEntity转换为DeviceDetail详情
     */
    public static DeviceDetailDTO entityToDTO(AggrDeviceEntity entity){
        DeviceDetailDTO detailDTO = new DeviceDetailDTO();
        detailDTO.setDeviceId(entity.getId().toString());
        detailDTO.setDeviceName(entity.getDeviceName());
        detailDTO.setCnName(entity.getCnName());
        detailDTO.setProductId(entity.getProductId().toString());
        detailDTO.setProductName(entity.getProductName());
        detailDTO.setDeviceType(entity.getDeviceType().getCode());
        detailDTO.setRegion(entity.getRegion());
        detailDTO.setAuthType(entity.getAuthType());
        detailDTO.setIpAddr(entity.getIpAddr());
        detailDTO.setFwVersion(entity.getFwVersion());
        detailDTO.setActiveTime(entity.getActiveTime());
        detailDTO.setLastOnlineTime(entity.getLastOnlineTime());
        detailDTO.setDeviceStatus(entity.getDeviceStatus().getCode());
        detailDTO.setSdkType(entity.getSdkType());
        detailDTO.setSdkVersion(entity.getSdkVersion());
        if(entity.getDeviceSercetEntity() != null){
            detailDTO.setProductKey(entity.getDeviceSercetEntity().getProductKey());
            detailDTO.setDeviceSecret(entity.getDeviceSercetEntity().getDeviceSecret());
            detailDTO.setProductSecret(entity.getDeviceSercetEntity().getProductSecret());
            detailDTO.setAutoActive(entity.getDeviceSercetEntity().getAutoActive());
        }

        return detailDTO;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getFwVersion() {
        return fwVersion;
    }

    public void setFwVersion(String fwVersion) {
        this.fwVersion = fwVersion;
    }

    public Long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Long activeTime) {
        this.activeTime = activeTime;
    }

    public Long getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Long lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getSdkType() {
        return sdkType;
    }

    public void setSdkType(String sdkType) {
        this.sdkType = sdkType;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
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
        return "DeviceDetailDTO{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", cnName='" + cnName + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", deviceType=" + deviceType +
                ", region='" + region + '\'' +
                ", authType='" + authType + '\'' +
                ", ipAddr='" + ipAddr + '\'' +
                ", fwVersion='" + fwVersion + '\'' +
                ", activeTime=" + activeTime +
                ", lastOnlineTime=" + lastOnlineTime +
                ", deviceStatus=" + deviceStatus +
                ", sdkType='" + sdkType + '\'' +
                ", sdkVersion='" + sdkVersion + '\'' +
                ", productKey='" + productKey + '\'' +
                ", deviceSecret='" + deviceSecret + '\'' +
                ", productSecret='" + productSecret + '\'' +
                ", autoActive=" + autoActive +
                '}';
    }
}
