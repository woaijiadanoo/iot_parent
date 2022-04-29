package com.ruyuan.jiangzh.iot.user.interfaces.controller;

import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.user.domain.entity.Tenant;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.TenantRepository;
import com.ruyuan.jiangzh.iot.user.domain.vo.TenantId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TenantController {

    @Autowired
    private TenantRepository tenantRepository;

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
