package com.ruyuan.jiangzh.iot.user.temp;

import com.ruyuan.jiangzh.iot.base.enums.CommonEnums;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HeaderAuthenticationProcessingJWTFilter extends AbstractAuthenticationProcessingFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected HeaderAuthenticationProcessingJWTFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String username = request.getAttribute(CommonEnums.USERNAME_HEADER.getName()) +"";

        logger.info("HeaderAuthenticationProcessingJWTFilter username : [{}]", username);

        if(!IoTStringUtils.isBlank(username)){
            HeaderAuthenticationToken authenticationToken = new HeaderAuthenticationToken(username);
            return this.getAuthenticationManager().authenticate(authenticationToken);
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 设置安全上下文
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        // 调用下一个provider
        chain.doFilter(request,response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
    }
}
