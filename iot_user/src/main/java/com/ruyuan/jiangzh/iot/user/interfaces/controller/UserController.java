package com.ruyuan.jiangzh.iot.user.interfaces.controller;

import com.google.gson.Gson;
import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.web.BaseController;
import com.ruyuan.jiangzh.iot.base.web.RespCodeEnum;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.user.domain.entity.SecurityUser;
import com.ruyuan.jiangzh.iot.user.domain.entity.UserEntity;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.UserRepository;
import com.ruyuan.jiangzh.iot.user.domain.vo.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class UserController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;
    /*
        http://localhost:8081/api/users?ruyuan_name=ruyuan_00
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public RespDTO getUsers(){

        SecurityUser currentUser = currentUser();
        logger.info("currentUser : [{}]", new Gson().toJson(currentUser));

        UserId userId = new UserId(toUUID("940f0e60-c8a0-11ec-989e-8b76480d43cf"));
        UserEntity userById = userRepository.findUserById(userId);

        return RespDTO.success(userById);
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
