package com.ruyuan.jiangzh.iot.user.controller;

import com.ruyuan.jiangzh.iot.user.dao.entity.IotUser;
import com.ruyuan.jiangzh.iot.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getall",method = RequestMethod.GET)
    public List<IotUser> getUser(){
        List<IotUser> iotUsers = userService.showXml();

        return iotUsers;
    }

}
