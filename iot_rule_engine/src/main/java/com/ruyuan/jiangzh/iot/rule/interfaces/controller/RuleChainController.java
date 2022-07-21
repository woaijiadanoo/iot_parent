package com.ruyuan.jiangzh.iot.rule.interfaces.controller;

import com.ruyuan.jiangzh.iot.base.security.IoTSecurityUser;
import com.ruyuan.jiangzh.iot.base.web.BaseController;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class RuleChainController extends BaseController {

    /*
        http://localhost:8083/api/v1/test?ruyuan_name=ruyuan_00
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public RespDTO test(){
        IoTSecurityUser currentUser = getCurrentUser();

        return RespDTO.success(currentUser);
    }

}
