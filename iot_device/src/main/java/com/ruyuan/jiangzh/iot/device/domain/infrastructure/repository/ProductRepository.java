package com.ruyuan.jiangzh.iot.device.domain.infrastructure.repository;

import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.device.domain.entity.ProductEntity;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.ProductStatusEnums;
import com.ruyuan.jiangzh.iot.base.uuid.device.ProductId;

public interface ProductRepository {

    /*
        新增和修改都是一个
     */
    ProductEntity saveProduct(ProductEntity product);

    /*
        删除Product
     */
    boolean delProduct(ProductId productId);

    /*
        按ID查询
     */
    ProductEntity findProductById(ProductId productId);

    /*
        列表查询
     */
    PageDTO<ProductEntity> products(PageDTO<ProductEntity> pageDTO);

    /*
        修改是否允许自动激活状态
     */
    ProductEntity updateAutoActive(ProductId productId, boolean autoActive);

    /*
        修改产品的发布状态
     */
    ProductEntity updateProductStatus(ProductId productId, ProductStatusEnums productStatus);

}
