package com.ruyuan.jiangzh.iot.gateway.controller;

import com.ruyuan.jiangzh.service.sdk.UserServiceAPI;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/*
    http://localhost:8082/gw/api/user/getAll
 */
@RestController
@RequestMapping("/gw/api/user")
public class UserController {

    @DubboReference()
    private UserServiceAPI userServiceAPI;

    @RequestMapping(value = "/getAll",method = RequestMethod.GET)
    public String getAll(){
        return userServiceAPI.getUserAll();
    }

}
