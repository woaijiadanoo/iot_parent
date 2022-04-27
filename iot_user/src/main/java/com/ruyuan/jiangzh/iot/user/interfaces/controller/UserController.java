package com.ruyuan.jiangzh.iot.user.interfaces.controller;

import com.google.gson.Gson;
import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.web.RespCodeEnum;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.user.domain.entity.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /*
        http://localhost:8081/api/users

        header: ruyuan_name : jiangzh
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public RespDTO getUsers(){

        SecurityUser currentUser = currentUser();

        logger.info("currentUser : [{}]", new Gson().toJson(currentUser));

        return RespDTO.success();
    }

    public SecurityUser currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser userSecurity = null;
        if(authentication.getPrincipal() instanceof SecurityUser){
            userSecurity = (SecurityUser)authentication.getPrincipal();
        }
        if(userSecurity == null){
            throw new AppException(RespCodeEnum.PERMISSION_DENIED.getCode() , "user.premission_denied");
        }
        return userSecurity;
    }

}
