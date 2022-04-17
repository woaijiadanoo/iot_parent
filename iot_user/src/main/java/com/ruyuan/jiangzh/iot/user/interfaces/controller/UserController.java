package com.ruyuan.jiangzh.iot.user.interfaces.controller;

import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class UserController {

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public RespDTO getUsers(){

        return RespDTO.success();
    }

}
