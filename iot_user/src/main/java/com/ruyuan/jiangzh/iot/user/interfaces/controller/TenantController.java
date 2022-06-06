package com.ruyuan.jiangzh.iot.user.interfaces.controller;

import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.web.BaseController;
import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.base.web.RespCodeEnum;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.user.domain.entity.SecurityUser;
import com.ruyuan.jiangzh.iot.user.domain.entity.Tenant;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.TenantRepository;
import com.ruyuan.jiangzh.iot.user.domain.vo.TenantId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TenantController  extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TenantRepository tenantRepository;

    private static final String VALIDATE_TENANT_ID_MSG_ID = "user.tenant_id_is_empty";
    private static final String PARAMTER_IS_NULL_MSG_ID = "user.resource_is_empty";
    private static final String PREMISSION_DENIED = "user.premission_denied";

    /*
        http://localhost:8081/api/v1/tenant?ruyuan_name=jiangzh
        {
            "email":"jz01@gmail.com",
            "phone":"13822222222",
            "name":"jz01"
        }
     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN')")
    @RequestMapping(value = "/tenant", method = RequestMethod.POST)
    public RespDTO saveTenant(@RequestBody Tenant tenant){
        try {
            // save本身代表了新增或者修改
            boolean newTenant = tenant.getId() == null;
            if(newTenant){
                // 这里是新增
                Tenant result = tenantRepository.saveTenant(tenant);
                return RespDTO.success(checkNotNull(result));
            }else{
                // TODO , 这里是修改
                return RespDTO.success();
            }
        } catch (Exception e){
            // 报警并记录日志
            logger.error("saveTenant error:[{}]", e.getMessage());
            // 封装成自定义异常
            return RespDTO.systemFailture(e);
        }
    }

    /*
        http://localhost:8081/api/v1/tenant/073d0ca0-c7cc-11ec-99ce-614a2c1ca286?ruyuan_name=jiangzh
     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN')")
    @RequestMapping(value = "/tenant/{tenantId}", method = RequestMethod.DELETE)
    public RespDTO deleteTenant(@PathVariable("tenantId") String tenantIdStr){
        checkParameter(tenantIdStr, PARAMTER_IS_NULL_MSG_ID);
        try {
            TenantId tenantId = new TenantId(toUUID(tenantIdStr));
            if(tenantRepository.delTenant(tenantId)){
                return RespDTO.success();
            }else{
                return RespDTO.failture(RespCodeEnum.DELETE_FAILTURE.getCode(), "tenant.delete_failture");
            }
        } catch (Exception e){
            // 报警并记录日志
            logger.error("deleteTenant error:[{}]", e.getMessage());
            // 封装成自定义异常
            return RespDTO.systemFailture(e);
        }
    }

    /*
        http://localhost:8081/api/v1/tenant/073d0ca0-c7cc-11ec-99ce-614a2c1ca286?ruyuan_name=jiangzh
     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN')")
    @RequestMapping(value = "/tenant/{tenantId}", method = RequestMethod.GET)
    public RespDTO getTenantById(@PathVariable("tenantId") String tenantIdStr){
        // 参数校验，尤其是非空验证
        // param_1：待校验的参数，param_2: msgId
        checkParameter(tenantIdStr, PARAMTER_IS_NULL_MSG_ID);
        try {
            // toUUID就是将字符串转换为UUID
            TenantId tenantId = new TenantId(toUUID(tenantIdStr));
            // 检查TenantId的合法性
            checkTenantId(tenantId);
            Tenant tenant = tenantRepository.findTenantById(tenantId);
            // 检查返回见过是不是为空
            Tenant result = checkNotNull(tenant);
            return RespDTO.success(result);
        }catch (Exception e){
            // 报警并记录日志
            logger.error("getTenantById error:[{}]", e.getMessage());
            // 封装成自定义异常
            return RespDTO.systemFailture(e);
        }
    }

    /*
        http://localhost:8081/api/v1/tenants?ruyuan_name=jiangzh
        http://localhost:8081/api/v1/tenants?ruyuan_name=jiangzh&pageSize=5
        http://localhost:8081/api/v1/tenants?ruyuan_name=jiangzh&nowPage=2&pageSize=5
        http://localhost:8081/api/v1/tenants?ruyuan_name=jiangzh&nowPage=2&pageSize=5&email=gmail
        http://localhost:8081/api/v1/tenants?ruyuan_name=jiangzh&nowPage=2&pageSize=5&email=gmail&phone=13866666666
     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN')")
    @RequestMapping(value = "/tenants", method = RequestMethod.GET)
    public RespDTO tenants(
            @RequestParam(name = "nowPage", required = false, defaultValue = "1") long nowPage,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") long pageSize,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "phone", required = false) String phone){
        try {
            // 组织page对象
            PageDTO<Tenant> pageDTO = new PageDTO<>(nowPage,pageSize);
            spellCondition(pageDTO,"email", email);
            spellCondition(pageDTO,"phone", phone);

            // 请求业务数据返回值
            PageDTO<Tenant> tenants = tenantRepository.tenants(pageDTO);

            return RespDTO.success(tenants);
        } catch (Exception e){
            // 报警并记录日志
            logger.error("tenants error:[{}]", e.getMessage());
            // 封装成自定义异常
            return RespDTO.systemFailture(e);
        }

    }

    // 检查TenantId的合法性
    private void checkTenantId(TenantId tenantId){
        // 是否为空或者合法
        validateId(tenantId, VALIDATE_TENANT_ID_MSG_ID);
        // 检查当前用户与待查询的tenantId是否匹配
        SecurityUser securityUser = currentUser();
//        if(securityUser.getTenantId() == null || !securityUser.getTenantId().equals(tenantId)){
//            throw new AppException(RespCodeEnum.PERMISSION_DENIED.getCode(), PREMISSION_DENIED);
//        }
    }

    // 获取当前用户
    private SecurityUser currentUser(){
        SecurityUser securityUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof SecurityUser){
            securityUser = (SecurityUser)authentication.getPrincipal();
        }else{
            throw new AppException(RespCodeEnum.PERMISSION_DENIED.getCode(), PREMISSION_DENIED);
        }
        return securityUser;
    }



    /*
        http://localhost:8081/api/tenant/test

        ruyuan_name : jiangzh

        {
            "code": 200,
            "requestId": "7631787cb48b48d5a5b51d41b5c61eb1",
            "message": "",
            "state": "",
            "data": {
                "id": {
                    "uuid": "073d0ca0-c7cc-11ec-99ce-614a2c1ca286",
                    "entityType": "TENANT"
                },
                "createTime": 1651243965034,
                "email": "jz@gmail.com",
                "phone": "13811111111",
                "name": "jiangzh"
            }
        }

     */
    @RequestMapping(value = "/api/tenant/test", method = RequestMethod.GET)
    public RespDTO getTenant(){
        TenantId tenantId = new TenantId(UUIDHelper.genUuid());
        Tenant tenant = new Tenant(tenantId);
        tenant.setName("jiangzh");
        tenant.setEmail("jz@gmail.com");
        tenant.setPhone("13811111111");

        tenantRepository.saveTenant(tenant);

        Tenant tenantById = tenantRepository.findTenantById(tenantId);

        return RespDTO.success(tenantById);
    }

}
