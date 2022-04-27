package com.ruyuan.jiangzh.iot.user.infrastructure.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruyuan.jiangzh.iot.base.web.RespCodeEnum;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
    SpringSecurity统一异常处理Handler
 */
@Component
public class IoTErrorResponseHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            if(!response.isCommitted()){
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                objectMapper.writeValue(response.getWriter(),
                        RespDTO.failture(RespCodeEnum.PERMISSION_DENIED.getCode(), "You do not have permission to perform this operation"));
            }
    }

}
