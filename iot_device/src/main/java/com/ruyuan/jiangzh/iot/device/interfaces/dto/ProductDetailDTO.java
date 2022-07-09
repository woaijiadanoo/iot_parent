package com.ruyuan.jiangzh.iot.device.interfaces.dto;

import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.device.domain.entity.ProductEntity;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceTypeEnums;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.NetTypeEnums;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.ProductStatusEnums;
import com.ruyuan.jiangzh.iot.base.uuid.device.ProductId;

import java.util.UUID;

public class ProductDetailDTO {

    private String uuid;
    private String productName;
    private String productKey;
    private Long createTime;
    private String productType;
    private Integer deviceType;
    private Integer netType;
    private Integer dataFormat;
    private Integer dataCheckLevel;
    private String authType;
    private String productDesc;
    private Boolean autoActive;
    private Integer productStatus;
    private String productSecret;

    public static ProductDetailDTO entityToDTO(ProductEntity entity){
        ProductDetailDTO detail = new ProductDetailDTO();
        detail.setUuid(entity.getId().toString());
        detail.setProductName(entity.getProductName());
        detail.setProductKey(entity.getProductKey());
        detail.setCreateTime(entity.getCreateTime());
        detail.setProductType(entity.getProductType());
        detail.setDeviceType(entity.getDeviceType().getCode());
        detail.setNetType(entity.getNetType().getCode());
        detail.setDataFormat(entity.getDataFormat());
        detail.setDataCheckLevel(entity.getDataCheckLevel());
        detail.setAuthType(entity.getAuthType());
        detail.setProductDesc(entity.getProductDesc());
        detail.setAutoActive(entity.isAutoActive());
        detail.setProductStatus(entity.getProductStatus().getCode());
        detail.setProductSecret(entity.getProductSecret());

        return detail;
    }

    public ProductEntity dtoToEntity(UUID tenantId, UUID userId){
        ProductEntity product = new ProductEntity();
        if(!IoTStringUtils.isBlank(this.getUuid())){
            product.setId(new ProductId(IoTStringUtils.toUUID(this.getUuid())));
        }
        product.setTenantId(tenantId);
        product.setUserId(userId);
        if(this.getProductName() != null){
            product.setProductName(this.getProductName());
        }

        product.setProductType(this.getProductType());
        if(this.getDeviceType() != null){
            product.setDeviceType(DeviceTypeEnums.getByCode(this.getDeviceType()));
        }
        if(this.getNetType() != null){
            product.setNetType(NetTypeEnums.getByCode(this.getNetType()));
        }
        // 我们只有一个协议
        product.setDataFormat(0);
        // 默认直接校验
        product.setDataCheckLevel(1);
        // 默认必须验证
        product.setAuthType("secretKey");
        product.setProductDesc(this.getProductDesc());
        product.setAutoActive(this.getAutoActive());
        if(this.getProductStatus() != null){
            product.setProductStatus(ProductStatusEnums.getByCode(this.getProductStatus()));
        }
        if(this.getProductKey() != null){
            product.setProductKey(this.getProductKey());
        }
        if(this.getProductSecret() != null){
            product.setProductSecret(this.getProductSecret());
        }

        return product;
    }



    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
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

    public String getProductSecret() {
        return productSecret;
    }

    public void setProductSecret(String productSecret) {
        this.productSecret = productSecret;
    }

    @Override
    public String toString() {
        return "ProductDetailDTO{" +
                "uuid='" + uuid + '\'' +
                ", productName='" + productName + '\'' +
                ", productKey='" + productKey + '\'' +
                ", createTime=" + createTime +
                ", productType='" + productType + '\'' +
                ", deviceType=" + deviceType +
                ", netType=" + netType +
                ", dataFormat=" + dataFormat +
                ", dataCheckLevel=" + dataCheckLevel +
                ", authType='" + authType + '\'' +
                ", productDesc='" + productDesc + '\'' +
                ", autoActive=" + autoActive +
                ", productStatus=" + productStatus +
                ", productSecret='" + productSecret + '\'' +
                '}';
    }
}
