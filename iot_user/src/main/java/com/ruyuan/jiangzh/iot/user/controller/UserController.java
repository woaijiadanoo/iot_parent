package com.ruyuan.jiangzh.iot.user.controller;

import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.web.BaseController;
import com.ruyuan.jiangzh.iot.base.web.RespCodeEnum;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.common.ThreadLocalUtils;
import com.ruyuan.jiangzh.iot.user.dao.entity.IotUser;
import com.ruyuan.jiangzh.iot.user.service.UserService;
import org.apache.tomcat.jni.Thread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *  http://localhost:8081/api/user/getall
     * @return
     */
    @RequestMapping(value = "/getall",method = RequestMethod.GET)
    public RespDTO getUser(){
        List<IotUser> iotUsers = userService.showXml();

        System.out.println("controller requestId: "+ IoTStringUtils.requestId());

        // 假装有传入一个username
        String msgId = "user.username.ugly";
        String username = "Allen";

        logger.info("controller log msgId:[{}], username:[{}] ",msgId, username);
        Object[] params = new Object[]{username};
        if(username.trim().length() >  0){
            throw new AppException(RespCodeEnum.RESOURCE_NOT_EXISTS.getCode(), msgId,params);
        }

        return RespDTO.success(iotUsers);
    }

}
