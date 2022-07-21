package com.ruyuan.jiangzh.iot.device.interfaces.controller;

import com.ruyuan.jiangzh.iot.base.security.IoTSecurityUser;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.web.BaseController;
import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.base.web.RespCodeEnum;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.common.AuthorityRole;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.device.domain.domainservice.DeviceDomainService;
import com.ruyuan.jiangzh.iot.device.domain.entity.ProductEntity;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.ProductStatusEnums;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.repository.ProductRepository;
import com.ruyuan.jiangzh.iot.base.uuid.device.ProductId;
import com.ruyuan.jiangzh.iot.device.interfaces.dto.ProductDTO;
import com.ruyuan.jiangzh.iot.device.interfaces.dto.ProductDetailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1")
public class ProductController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DeviceDomainService deviceDomainService;

    private static final String PRODUCT_NAME_NOT_EXISTS="product.product_name_not_exists";


    /*
        只有给我们组内的前端使用的时候才把新增和修改放在一起。
        http://localhost:8082/api/v1/product?ruyuan_name=ruyuan_00
        {
            "productName":"1",
            "productType":"1",
            "deviceType":"0",
            "netType":"0",
            "productDesc":"11",
            "autoActive":"true",
            "productStatus": 0
        }

     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN', 'USER')")
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public RespDTO saveProduct(@RequestBody ProductDetailDTO detailDTO){
        // 需要验证的参数逐一验证
        checkParameter(detailDTO.getProductName(), PRODUCT_NAME_NOT_EXISTS);

        // 获取当前用户
        IoTSecurityUser currentUser = getCurrentUser();

        ProductEntity productEntity = productRepository.saveProduct(detailDTO.dtoToEntity(currentUser.getTenantId(), currentUser.getUserId()));

        // 组织返回值
        return RespDTO.success(checkNotNull(ProductDetailDTO.entityToDTO(productEntity)));
    }

    /*
        http://localhost:8082/api/v1/products?ruyuan_name=ruyuan_00
     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN', 'USER')")
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public RespDTO findProducts(
            @RequestParam(name = "nowPage", required = false, defaultValue = "1") long nowPage,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") long pageSize,
            @RequestParam(name = "productName", required = false) String productName){
        // 组织分页条件参数
        PageDTO<ProductEntity> input = new PageDTO<>(nowPage, pageSize);
        if(!IoTStringUtils.isBlank(productName)){
            input.spellCondition("productName",  productName);
        }

        // 组织权限参数
        /*
            SYS_ADMIN 应该能看见所有的product列表
            TENANT_ADMIN : 应该能看见当前tenant下的所有product
            USER: 只能看见当前tenant和该user创建的product
         */
        IoTSecurityUser currentUser = getCurrentUser();
        if(currentUser.getAuthorityRole().equals(AuthorityRole.TENANT_ADMIN)){
            input.spellCondition("tenantId",  UUIDHelper.fromTimeUUID(currentUser.getTenantId().getUuid()));
        }else if(currentUser.getAuthorityRole().equals(AuthorityRole.USER)){
            input.spellCondition("tenantId",  UUIDHelper.fromTimeUUID(currentUser.getTenantId().getUuid()));
            input.spellCondition("userId",  UUIDHelper.fromTimeUUID(currentUser.getUserId().getUuid()));
        }

        // 访问repository，并获取返回值
        PageDTO products = productRepository.products(input);
        List<ProductEntity> records = products.getRecords();

        // 将entity转换为DTO
        List<ProductDTO> productDTOS = null;
        if(records != null && records.size() > 0){
            productDTOS = records.stream().map(e -> ProductDTO.entityToDTO(e)).collect(Collectors.toList());
        }
        products.setRecords(productDTOS);
        // 返回DTO数据
        return RespDTO.success(products);
    }


    /*
        http://localhost:8082/api/v1/product/684929e0-d1d4-11ec-85a0-1becaaf701a4?ruyuan_name=ruyuan_00
     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN', 'USER')")
    @RequestMapping(value = "/product/{productId}", method = RequestMethod.GET)
    public RespDTO findProductById(@PathVariable(value = "productId")String productIdStr){
        ProductId productId = new ProductId(toUUID(productIdStr));
        ProductEntity entity = productRepository.findProductById(productId);
        /*
            根据用户角色不同，TENANT_ADMIN可以查看同Tenant下的数据，USER只能查自己创建的数据
         */
        if(entity != null && entity.getId() != null){
            IoTSecurityUser currentUser = getCurrentUser();
            if(entity.getTenantId().equals(currentUser.getTenantId())){
                if(currentUser.getAuthorityRole().equals(AuthorityRole.TENANT_ADMIN)){
                    return RespDTO.success(ProductDetailDTO.entityToDTO(entity));
                }else if(currentUser.getAuthorityRole().equals(AuthorityRole.USER)){
                    if(entity.getUserId().equals(currentUser.getUserId())){
                        return RespDTO.success(ProductDetailDTO.entityToDTO(entity));
                    }
                }
            }
        }
        return RespDTO.failture(RespCodeEnum.PERMISSION_DENIED.getCode(), PREMISSION_DENIED);
    }


    /*
        http://localhost:8082/api/v1/product/684929e0-d1d4-11ec-85a0-1becaaf701a4?ruyuan_name=ruyuan_00
     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN', 'USER')")
    @RequestMapping(value = "/product/{productId}", method = RequestMethod.DELETE)
    public RespDTO delProductById(@PathVariable(value = "productId")String productIdStr){
        ProductId productId = new ProductId(toUUID(productIdStr));
        ProductEntity entity = productRepository.findProductById(productId);
        /*
            根据用户角色不同，TENANT_ADMIN可以查看同Tenant下的数据，USER只能查自己创建的数据
         */
        if(entity != null && entity.getId() != null){
            IoTSecurityUser currentUser = getCurrentUser();
            if(entity.getTenantId().equals(currentUser.getTenantId())){
                if(currentUser.getAuthorityRole().equals(AuthorityRole.TENANT_ADMIN)){
                    productRepository.delProduct(productId);
                    return RespDTO.success();
                }else if(currentUser.getAuthorityRole().equals(AuthorityRole.USER)){
                    if(entity.getUserId().equals(currentUser.getUserId())){
                        productRepository.delProduct(productId);
                        return RespDTO.success();
                    }
                }
            }
        }
        return RespDTO.failture(RespCodeEnum.PERMISSION_DENIED.getCode(), PREMISSION_DENIED);
    }


    /*
        http://localhost:8082/api/v1/product/9a97f910-d28f-11ec-a05f-dbd50d7d93eb:updateAutoActive?ruyuan_name=ruyuan_00
        {
            "autoActive" : "false"
        }
        UUID: 9a97f910-d28f-11ec-a05f-dbd50d7d93eb
     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN', 'USER')")
    @RequestMapping(value = "/product/{productId}:updateAutoActive", method = RequestMethod.PUT)
    public RespDTO updateAutoActive(
                                        @PathVariable(value = "productId")String productIdStr,
                                        @RequestBody ProductDetailDTO productDetailDTO){
        boolean autoAcitve = productDetailDTO.getAutoActive();
        logger.info("updateAutoActive autoAcitve:[{}]", autoAcitve);

        ProductId productId = new ProductId(toUUID(productIdStr));
        ProductEntity entity = productRepository.findProductById(productId);
        /*
            根据用户角色不同，TENANT_ADMIN可以查看同Tenant下的数据，USER只能查自己创建的数据
         */
        if(entity != null && entity.getId() != null){
            IoTSecurityUser currentUser = getCurrentUser();
            if(entity.getTenantId().equals(currentUser.getTenantId())){
                if(currentUser.getAuthorityRole().equals(AuthorityRole.TENANT_ADMIN)){
                    deviceDomainService.updateAutoActive(productId, autoAcitve);
                    return RespDTO.success();
                }else if(currentUser.getAuthorityRole().equals(AuthorityRole.USER)){
                    if(entity.getUserId().equals(currentUser.getUserId())){
                        deviceDomainService.updateAutoActive(productId, autoAcitve);
                        return RespDTO.success();
                    }
                }
            }
        }
        return RespDTO.failture(RespCodeEnum.PERMISSION_DENIED.getCode(), PREMISSION_DENIED);

    }


    /*
        http://localhost:8082/api/v1/product/9a97f910-d28f-11ec-a05f-dbd50d7d93eb:updateProductStatus?ruyuan_name=ruyuan_00
        {
            "productStatus" : "1"
        }
     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN', 'USER')")
    @RequestMapping(value = "/product/{productId}:updateProductStatus", method = RequestMethod.PUT)
    public RespDTO updateProjectStatus(
            @PathVariable(value = "productId")String productIdStr,
            @RequestBody ProductDetailDTO productDetailDTO){
        Integer productStatus = productDetailDTO.getProductStatus();
        logger.info("updateProjectStatus productStatus:[{}]", productStatus);

        ProductId productId = new ProductId(toUUID(productIdStr));
        ProductEntity entity = productRepository.findProductById(productId);
        /*
            根据用户角色不同，TENANT_ADMIN可以查看同Tenant下的数据，USER只能查自己创建的数据
         */
        if(entity != null && entity.getId() != null){
            IoTSecurityUser currentUser = getCurrentUser();
            if(entity.getTenantId().equals(currentUser.getTenantId())){
                if(currentUser.getAuthorityRole().equals(AuthorityRole.TENANT_ADMIN)){
                    productRepository.updateProductStatus(productId, ProductStatusEnums.getByCode(productStatus));
                    return RespDTO.success();
                }else if(currentUser.getAuthorityRole().equals(AuthorityRole.USER)){
                    if(entity.getUserId().equals(currentUser.getUserId())){
                        productRepository.updateProductStatus(productId, ProductStatusEnums.getByCode(productStatus));
                        return RespDTO.success();
                    }
                }
            }
        }
        return RespDTO.failture(RespCodeEnum.PERMISSION_DENIED.getCode(), PREMISSION_DENIED);

    }

}
