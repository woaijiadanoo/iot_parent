package com.ruyuan.jiangzh.iot.filters;

import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.common.ThreadLocalUtils;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author jiangzheng
 * @version 1.0
 * @description: 链路追踪filter
 */
@Order(0)
@Component
@ConditionalOnProperty(name = "filters.traceId", havingValue = "true")
public class TraceIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String requestId = IoTStringUtils.genRequestId();
        // 初始化Threadlocal获取requestid
        ThreadLocalUtils.init(requestId);
        // 让日志能获取requestId作为traceId追踪使用
        MDC.put("iot_trace_id",requestId);
        // 后续业务请求
        filterChain.doFilter(servletRequest,servletResponse);

        ThreadLocalUtils.close();

    }
}
