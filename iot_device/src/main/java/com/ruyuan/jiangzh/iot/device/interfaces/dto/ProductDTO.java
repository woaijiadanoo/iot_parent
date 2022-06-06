package com.ruyuan.jiangzh.iot.device.interfaces.dto;

import com.ruyuan.jiangzh.iot.device.domain.entity.ProductEntity;

public class ProductDTO {

    private String uuid;
    private String productName;
    private String productKey;
    private Long createTime;
    private Integer deviceType;

    public static ProductDTO entityToDTO(ProductEntity entity){
        ProductDTO product = new ProductDTO();
        product.setUuid(entity.getId().toString());
        product.setProductName(entity.getProductName());
        product.setProductKey(entity.getProductKey());
        product.setCreateTime(entity.getCreateTime());
        product.setDeviceType(entity.getDeviceType().getCode());

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

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "uuid='" + uuid + '\'' +
                ", productName='" + productName + '\'' +
                ", productKey='" + productKey + '\'' +
                ", createTime=" + createTime +
                ", nodeType=" + deviceType +
                '}';
    }
}
