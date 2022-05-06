package com.ruyuan.jiangzh.iot.device.infrastructure.configs;

import com.ruyuan.jiangzh.iot.device.infrastructure.auth.HeaderAuthenticationPathRequestMatcher;
import com.ruyuan.jiangzh.iot.device.infrastructure.auth.HeaderAuthenticationProcessingFilter;
import com.ruyuan.jiangzh.iot.device.infrastructure.auth.HeaderAuthenticationProvider;
import com.ruyuan.jiangzh.iot.device.infrastructure.auth.IoTErrorResponseHandler;
import com.ruyuan.jiangzh.service.sdk.UserServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.BASIC_AUTH_ORDER - 100)
public class IoTSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    public static final String SERVICE_API_AUTH_ENTER_POINT = "/api/**";
    public static final String USER_REGISTRATION_ENTER_POINT = "/api/user:register";

    @Autowired
    private IoTErrorResponseHandler errorResponseHandler;

    @Resource
    private UserServiceAPI userServiceAPI;

    /*
        初始化ProcessingFilter

        单独从paramter中获取用户名
     */
    @Bean
    public HeaderAuthenticationProcessingFilter headerAuthenticationProcessingFilter() throws Exception {
        List<String> matchPath = new ArrayList<>(Arrays.asList(SERVICE_API_AUTH_ENTER_POINT));

        HeaderAuthenticationPathRequestMatcher requestMatcher = new HeaderAuthenticationPathRequestMatcher(matchPath);


        HeaderAuthenticationProcessingFilter headerAuthenticationProcessingFilter = new HeaderAuthenticationProcessingFilter(requestMatcher);

        headerAuthenticationProcessingFilter.setAuthenticationManager(authenticationManagerBean());

        return headerAuthenticationProcessingFilter;
    }

    /*
       初始化authenticationManager
    */

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /*
            初始化AuthenticationProvider
         */
    @Bean
    public HeaderAuthenticationProvider headerAuthenticationProvider(){
        HeaderAuthenticationProvider headerAuthenticationProvider = new HeaderAuthenticationProvider(userServiceAPI);
        return headerAuthenticationProvider;
    }

    /*
        将自定义provider加入到AuthenticationManager
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(headerAuthenticationProvider());
    }

    /*
        配置请求相关的内容
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().cacheControl().and().frameOptions().disable()
                .and()
                .cors()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(USER_REGISTRATION_ENTER_POINT).permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(SERVICE_API_AUTH_ENTER_POINT).authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(errorResponseHandler)
                .and()
                .addFilterBefore(headerAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}
