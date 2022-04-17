package com.ruyuan.jiangzh.iot.filters;

import com.ruyuan.jiangzh.iot.base.enums.CommonEnums;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.common.JWTUtils;
import com.ruyuan.jiangzh.iot.common.ThreadLocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author jiangzheng
 * @version 1.0
 * @description: JWT解析filter
 */
@Order(0)
@Component
@ConditionalOnProperty(name = "filters.jwt", havingValue = "true")
public class JWTFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("JWTFilter start");
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String jwtInfo = request.getHeader(JWTUtils.getJwtHeaderName());

        JWTUtils jwtUtils = new JWTUtils();
        String userName = jwtUtils.getUserNameInToken(jwtInfo);

        logger.info("jwtInfo:[{}], userName:[{}]", jwtInfo, userName);

        request.setAttribute(CommonEnums.USERNAME_HEADER.getName(), userName);

        // 后续业务请求
        filterChain.doFilter(servletRequest,servletResponse);


    }
}
