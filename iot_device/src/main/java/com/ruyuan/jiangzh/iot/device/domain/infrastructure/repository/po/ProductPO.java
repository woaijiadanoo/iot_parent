package com.ruyuan.jiangzh.iot.device.domain.infrastructure.repository.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author jiangzh

 */
@TableName(value = "product")
public class ProductPO extends Model<ProductPO> {

    private static final long serialVersionUID = 1L;
    @TableId
    private String uuid;

    private String userId;

    private String tenantId;

    private String productName;

    private String productType;

    private Integer deviceType;

    private Integer netType;

    private Integer dataFormat;

    private Integer dataCheckLevel;

    private String authType;

    private String productDesc;

    private Boolean autoActive;

    private Integer productStatus;

    private String productKey;

    private String productSecret;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getNetType() {
        return netType;
    }

    public void setNetType(Integer netType) {
        this.netType = netType;
    }

    public Integer getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(Integer dataFormat) {
        this.dataFormat = dataFormat;
    }

    public Integer getDataCheckLevel() {
        return dataCheckLevel;
    }

    public void setDataCheckLevel(Integer dataCheckLevel) {
        this.dataCheckLevel = dataCheckLevel;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public Boolean getAutoActive() {
        return autoActive;
    }

    public void setAutoActive(Boolean autoActive) {
        this.autoActive = autoActive;
    }

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
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

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Product{" +
        ", uuid=" + uuid +
        ", userId=" + userId +
        ", tenantId=" + tenantId +
        ", productName=" + productName +
        ", productType=" + productType +
        ", deviceType=" + deviceType +
        ", netType=" + netType +
        ", dataFormat=" + dataFormat +
        ", dataCheckLevel=" + dataCheckLevel +
        ", authType=" + authType +
        ", productDesc=" + productDesc +
        ", autoActive=" + autoActive +
        ", productStatus=" + productStatus +
        ", productKey=" + productKey +
        ", productSecret=" + productSecret +
        "}";
    }
}
