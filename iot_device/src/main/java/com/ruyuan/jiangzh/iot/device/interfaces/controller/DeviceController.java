package com.ruyuan.jiangzh.iot.device.interfaces.controller;

import com.ruyuan.jiangzh.iot.base.web.BaseController;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.base.security.IoTSecurityUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class DeviceController extends BaseController {

    /*
        http://localhost:8082/api/v1/devices?ruyuan_name=ruyuan_00
     */
    @RequestMapping(value = "devices", method = RequestMethod.GET)
    public RespDTO devices(){
        IoTSecurityUser currentUser = getCurrentUser();

        return RespDTO.success(currentUser);
    }

}
