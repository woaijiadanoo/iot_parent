package com.ruyuan.jiangzh.iot.gateway.interfaces.controller;

import com.google.common.collect.Maps;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.common.JWTUtils;
import com.ruyuan.jiangzh.iot.gateway.interfaces.dto.UserPwdDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class JWTController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /*
        http://localhost:8082/api/auth/login
        request:
        {
            "username":"allen",
            "userpwd":"1111"
        }

        response:
        {
            "code": 200,
            "requestId": "76d73bfac6c244bc8fb87ddf930a0a73",
            "message": "",
            "state": "",
            "data": {
                "randomKey": "zxhmqe",
                "jwt": "eyJhbGciOiJIUzUxMiJ9.eyJyYW5kb21LZXkiOiJ6eGhtcWUiLCJzdWIiOiJhbGxlbiIsImV4cCI6MTY1MDU1MDgyNywiaWF0IjoxNjQ5OTQ2MDI3fQ.Vxp9jFlZpmyRm6NajTgq9RSZcO6M6FsSbln2YtkK3EVpueR7Lq8BGWa9GmX92rmz99dXotnQI4n0exre1RaOpg"
            }
        }

     */
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public RespDTO login(@RequestBody UserPwdDTO userPwdDTO){
        // 通过用户名查询用户 信息
        // 省略
        // 根据pwd加密与数据库里的用户密码做匹配
        // 省略
        // 如果匹配成功，则生成JWT并返回
        logger.info("username : [{}]", userPwdDTO.getUsername());

        JWTUtils jwtUtils = new JWTUtils();
        String randomKey = jwtUtils.genRandomKey();
        String jwtStr = jwtUtils.genJWT(userPwdDTO.getUsername(), randomKey);

        Map<String,String> result = Maps.newHashMap();
        result.put("randomKey", randomKey);
        result.put("jwt", jwtStr);

        return RespDTO.success(result);
        // 如果匹配失败，说明用户名和密码不正确，需要返回错误信息
        // 省略
    }

}
