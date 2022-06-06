package com.ruyuan.jiangzh.iot.base.web;

import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.security.IoTSecurityUser;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDBased;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class BaseController {

    protected static final String PREMISSION_DENIED = "default.premission_denied";

    /**
     *  统一的异常处理
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public RespDTO exceptionHandler(Exception ex, HttpServletRequest request){
        // 自定义业务异常
        if(ex instanceof AppException){
            AppException exception = (AppException)ex;
            return RespDTO.failture(exception.getCode(), exception.getMessage(), exception.getState());
        }

        // 兜底方案
        return RespDTO.systemFailture(ex);
    }


    protected <T> T checkNotNull(T reference){
        if(reference == null){
            throw  new AppException(RespCodeEnum.RESOURCE_NOT_EXISTS.getCode(), "user.resource_is_empty");
        }
        return reference;
    }

    // 是否为空或者合法
    protected  void validateId(UUIDBased id, String msgId){
        if(id == null || id.getUuid() == null){
            throw new AppException(RespCodeEnum.PERMISSION_DENIED.getCode(), msgId);
        }
    }

    // toUUID就是将字符串转换为UUID
    protected UUID toUUID(String uuidStr){
        return IoTStringUtils.toUUID(uuidStr);
    }

    // 参数校验，尤其是非空验证
    // param_1：待校验的参数，param_2: msgId
    protected  void checkParameter(String param, String msgId){
        if (IoTStringUtils.isBlank(param)) {
            throw new AppException(RespCodeEnum.PARAM_IS_NULL.getCode(), msgId);
        }
    }

    // 给PageDTO传入条件参数
    protected void spellCondition(PageDTO pageDTO, String fieldName, Object fieldValue){
        if(fieldValue != null){
            pageDTO.spellCondition(fieldName, fieldValue);
        }
    }

    // 获取当前用户
    public IoTSecurityUser getCurrentUser(){
        IoTSecurityUser securityUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof IoTSecurityUser){
            securityUser = (IoTSecurityUser)authentication.getPrincipal();
        }else{
            throw new AppException(RespCodeEnum.PERMISSION_DENIED.getCode(), PREMISSION_DENIED);
        }
        return securityUser;
    }

}
