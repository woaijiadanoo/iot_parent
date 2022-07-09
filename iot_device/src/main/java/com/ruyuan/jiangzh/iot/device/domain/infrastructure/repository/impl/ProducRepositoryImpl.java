package com.ruyuan.jiangzh.iot.device.domain.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.base.web.RespCodeEnum;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.device.domain.entity.ProductEntity;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.ProductStatusEnums;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.repository.ProductRepository;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.repository.impl.mapper.ProductMapper;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.repository.po.ProductPO;
import com.ruyuan.jiangzh.iot.base.uuid.device.ProductId;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProducRepositoryImpl implements ProductRepository {

    @Resource
    private ProductMapper productMapper;

    private static final int PRODUCT_KEY_LEN = 10;
    private static final int PRODUCT_SECRET_LEN = 6;
    private static final String MSG_RESOURCE_NOT_EXISTS="default.resource_not_exists";

    @Override
    public ProductEntity saveProduct(ProductEntity product) {
        boolean newProduct = product.getId() == null ? true : false;
        if(newProduct){
            // 新增的话， 有几个属性是没有的， 需要补齐
            product.setId(new ProductId(UUIDHelper.genUuid()));
            product.setProductKey(IoTStringUtils.getRandomString(PRODUCT_KEY_LEN));
            product.setProductSecret(IoTStringUtils.getRandomString(PRODUCT_SECRET_LEN));

            ProductPO productPO = ProductEntity.entityToPo(product);
            productMapper.insert(productPO);
        }else{
            ProductPO productPO = ProductEntity.entityToPo(product);
            productMapper.updateById(productPO);
        }
        return product;
    }

    @Override
    public boolean delProduct(ProductId productId) {
        // 这个是没有做逻辑删除，如果有兴趣可以加入逻辑删除
        int deleted = productMapper.deleteById(UUIDHelper.fromTimeUUID(productId.getUuid()));
        return true;
    }

    @Override
    public ProductEntity findProductById(ProductId productId) {
        ProductPO productPO = productMapper.selectById(UUIDHelper.fromTimeUUID(productId.getUuid()));
        return new ProductEntity(productPO);
    }

    @Override
    public PageDTO<ProductEntity> products(PageDTO<ProductEntity> pageDTO) {
        // 组织mybatis-plus的两个核心对象 ipage , queryWrapper
        IPage<ProductPO> iPage = new Page<>(pageDTO.getNowPage(),pageDTO.getPageSize());
        QueryWrapper<ProductPO> queryWrapper = null;
        if(pageDTO.getConditions().size() > 0){
            queryWrapper = new QueryWrapper<>();
            Set<String> keys = pageDTO.getConditions().keySet();
            for(String key : keys){
                // 拼接查询条件
                spellCondition(queryWrapper, key, pageDTO.getConditions().get(key));
            }
        }

        IPage<ProductPO> page = productMapper.selectPage(iPage, queryWrapper);
        List<ProductPO> records = page.getRecords();
        // 将PO转换为领域实体
        List<ProductEntity> entities =
                records.stream().map(po -> new ProductEntity(po)).collect(Collectors.toList());
        // 封装返回对象
        pageDTO.setResult(page.getTotal(), page.getPages(), entities);

        return pageDTO;
    }

    @Override
    public ProductEntity updateAutoActive(ProductId productId, boolean autoActive) {
        ProductPO productPO = productMapper.selectById(UUIDHelper.fromTimeUUID(productId.getUuid()));
        if(productPO == null){
            throw  new AppException(RespCodeEnum.RESOURCE_NOT_EXISTS.getCode(), MSG_RESOURCE_NOT_EXISTS);
        }

        productPO.setAutoActive(autoActive);
        productMapper.updateById(productPO);

        return new ProductEntity(productPO);
    }

    @Override
    public ProductEntity updateProductStatus(ProductId productId, ProductStatusEnums productStatus) {
        ProductPO productPO = productMapper.selectById(UUIDHelper.fromTimeUUID(productId.getUuid()));
        if(productPO == null){
            throw  new AppException(RespCodeEnum.RESOURCE_NOT_EXISTS.getCode(), MSG_RESOURCE_NOT_EXISTS);
        }

        productPO.setProductStatus(productStatus.getCode());
        productMapper.updateById(productPO);

        return new ProductEntity(productPO);
    }

    private void spellCondition(QueryWrapper<ProductPO> queryWrapper, String fieldName, Object fieldValue) {
        if("productName".equalsIgnoreCase(fieldName)){
            queryWrapper.eq("product_name", fieldValue);
        }else if("userId".equalsIgnoreCase(fieldName)){
            queryWrapper.eq("user_id", fieldValue);
        }else if("tenantId".equalsIgnoreCase(fieldName)){
            queryWrapper.eq("tenant_id", fieldValue);
        }
    }


}
