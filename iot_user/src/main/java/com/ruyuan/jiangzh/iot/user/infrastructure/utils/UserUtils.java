package com.ruyuan.jiangzh.iot.user.infrastructure.utils;

import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.web.RespCodeEnum;
import com.ruyuan.jiangzh.iot.user.domain.entity.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {

    private static final String PREMISSION_DENIED = "user.premission_denied";

    // 获取当前用户
    public SecurityUser getCurrentUser(){
        SecurityUser securityUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof SecurityUser){
            securityUser = (SecurityUser)authentication.getPrincipal();
        }else{
            throw new AppException(RespCodeEnum.PERMISSION_DENIED.getCode(), PREMISSION_DENIED);
        }
        return securityUser;
    }


}
