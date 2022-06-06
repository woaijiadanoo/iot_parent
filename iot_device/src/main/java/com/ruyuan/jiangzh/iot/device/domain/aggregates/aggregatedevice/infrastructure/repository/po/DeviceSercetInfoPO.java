package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
/**
 * <p>
 * 
 * </p>
 */
@TableName("device_sercet_info")
public class DeviceSercetInfoPO extends Model<DeviceSercetInfoPO> {

    private static final long serialVersionUID = 1L;

    @TableId
    private String uuid;

    private String deviceName;

    private String productKey;

    private String productSecret;

    private String deviceSecret;

    private Integer deviceStatus;
    private Boolean autoActive;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductSecret() {
        return productSecret;
    }

    public void setProductSecret(String productSecret) {
        this.productSecret = productSecret;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public void setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
    }

    public Boolean getAutoActive() {
        return autoActive;
    }

    public void setAutoActive(Boolean autoActive) {
        this.autoActive = autoActive;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    @Override
    public String toString() {
        return "DeviceSercetInfo{" +
        ", uuid=" + uuid +
        ", deviceName=" + deviceName +
        ", productKey=" + productKey +
        ", productSecret=" + productSecret +
        ", deviceSecret=" + deviceSecret +
        ", deviceStatus=" + deviceStatus +
        ", autoActive=" + autoActive +
        "}";
    }
}
