package com.ruyuan.jiangzh.iot.device.domain.entity;

import com.ruyuan.jiangzh.iot.base.uuid.CreateTimeIdBase;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceTypeEnums;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.NetTypeEnums;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.ProductStatusEnums;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.repository.po.ProductPO;
import com.ruyuan.jiangzh.iot.base.uuid.device.ProductId;

import java.io.Serializable;
import java.util.UUID;

public class ProductEntity  extends CreateTimeIdBase<ProductId> implements Serializable {
    // 租户信息
    private UUID tenantId;
    // 用户信息
    private UUID userId;
    // 产品名称
    private String productName;
    // 产品类型
    private String productType;
    // 节点类型【直连，网关，网关子设备】
    private DeviceTypeEnums deviceType;
    // 联网方式
    private NetTypeEnums netType;
    // 数据格式 0 - RuyuanLink
    private Integer dataFormat;
    // 数据校验级别 0-不校验 1-弱校验
    private Integer dataCheckLevel;
    // 认证方式 0-无认证 1-secretKey
    private String authType;
    // 产品描述
    private String productDesc;
    // 是否可以自动激活 动态状态【开启和关闭】
    private Boolean autoActive;
    // 产品状态【开发中，已发布】
    private ProductStatusEnums productStatus;
    private String productKey;
    private String productSecret;

    public ProductEntity(){}
    public ProductEntity(ProductId productId){super(productId);}
    public ProductEntity(ProductPO productPO){
        super(new ProductId(UUIDHelper.fromStringId(productPO.getUuid())));
        this.tenantId = UUIDHelper.fromStringId(productPO.getTenantId());
        this.userId = UUIDHelper.fromStringId(productPO.getUserId());
        this.productStatus = ProductStatusEnums.getByCode(productPO.getProductStatus());
        this.productType = productPO.getProductType();
        this.productSecret = productPO.getProductSecret();
        this.productName = productPO.getProductName();
        this.productKey = productPO.getProductKey();
        this.productDesc = productPO.getProductDesc();
        this.netType = NetTypeEnums.getByCode(productPO.getNetType());
        this.deviceType = DeviceTypeEnums.getByCode(productPO.getDeviceType());
        this.dataFormat = productPO.getDataFormat();
        this.dataCheckLevel = productPO.getDataCheckLevel();
        this.autoActive = productPO.getAutoActive();
        this.authType = productPO.getAuthType();
    }

    public static ProductPO entityToPo(ProductEntity product){
        ProductPO productPO = new ProductPO();
        productPO.setUuid(UUIDHelper.fromTimeUUID(product.getId().getUuid()));
        productPO.setUserId(UUIDHelper.fromTimeUUID(product.getUserId()));
        productPO.setTenantId(UUIDHelper.fromTimeUUID(product.getTenantId()));
        productPO.setProductStatus(product.getProductStatus().getCode());
        productPO.setNetType(product.getNetType().getCode());
        productPO.setDeviceType(product.getDeviceType().getCode());
        productPO.setDataFormat(product.getDataFormat());
        productPO.setDataCheckLevel(product.getDataCheckLevel());
        productPO.setAutoActive(product.isAutoActive());
        productPO.setAuthType(product.getAuthType());
        productPO.setProductDesc(product.getProductDesc());
        productPO.setProductKey(product.getProductKey());
        productPO.setProductSecret(product.getProductSecret());
        productPO.setProductName(product.getProductName());
        productPO.setProductType(product.getProductType());

        return productPO;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public DeviceTypeEnums getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceTypeEnums deviceType) {
        this.deviceType = deviceType;
    }

    public NetTypeEnums getNetType() {
        return netType;
    }

    public void setNetType(NetTypeEnums netType) {
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

    public Boolean isAutoActive() {
        return autoActive;
    }

    public void setAutoActive(Boolean autoActive) {
        this.autoActive = autoActive;
    }

    public ProductStatusEnums getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatusEnums productStatus) {
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
}
