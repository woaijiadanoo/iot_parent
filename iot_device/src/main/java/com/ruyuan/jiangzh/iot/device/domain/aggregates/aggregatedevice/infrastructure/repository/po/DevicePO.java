package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * <p>
 * 
 * </p>
 *
 */
@TableName("device")
public class DevicePO extends Model<DevicePO> {

    private static final long serialVersionUID = 1L;

    @TableId
    private String uuid;

    private String userId;

    private String tenantId;

    private String productId;

    private String productName;

    private Integer deviceType;

    private String reginName;

    private String deviceName;

    private String cnName;

    private String authType;

    private String ipAddr;

    private String fwVersion;

    private LocalDateTime activeTime;

    private LocalDateTime lastOnlineTime;

    private Integer deviceStatus;

    private String sdkType;

    private String sdkVersion;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    public String getReginName() {
        return reginName;
    }

    public void setReginName(String reginName) {
        this.reginName = reginName;
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

    public LocalDateTime getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(LocalDateTime activeTime) {
        this.activeTime = activeTime;
    }

    public LocalDateTime getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(LocalDateTime lastOnlineTime) {
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

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Device{" +
        ", uuid=" + uuid +
        ", userId=" + userId +
        ", tenantId=" + tenantId +
        ", productId=" + productId +
        ", productName=" + productName +
        ", deviceType=" + deviceType +
        ", reginName=" + reginName +
        ", deviceName=" + deviceName +
        ", cnName=" + cnName +
        ", authType=" + authType +
        ", ipAddr=" + ipAddr +
        ", fwVersion=" + fwVersion +
        ", activeTime=" + activeTime +
        ", lastOnlineTime=" + lastOnlineTime +
        ", deviceStatus=" + deviceStatus +
        ", sdkType=" + sdkType +
        ", sdkVersion=" + sdkVersion +
        "}";
    }
}
