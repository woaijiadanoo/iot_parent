package com.ruyuan.jiangzh.iot.user.controller;

import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.user.dao.entity.IotUser;
import com.ruyuan.jiangzh.iot.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     *  http://localhost:8081/api/user/getall
     * @return
     */
    @RequestMapping(value = "/getall",method = RequestMethod.GET)
    public RespDTO getUser(){
        List<IotUser> iotUsers = userService.showXml();

        // 假装有传入一个username
        String msgId = "username_is_null";
        String username = "";
        if(username.trim().length() == 0){
//            throw new AppException(msgId);
        }

        return RespDTO.success(iotUsers);
    }

}
