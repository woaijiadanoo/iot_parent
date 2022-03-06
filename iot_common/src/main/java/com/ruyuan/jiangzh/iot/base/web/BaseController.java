package com.ruyuan.jiangzh.iot.base.web;

import com.ruyuan.jiangzh.iot.base.exception.AppException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

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

}
