package com.ruyuan.jiangzh.iot.user.infrastructure.auth;

import com.ruyuan.jiangzh.iot.user.domain.entity.UserEntity;
import com.ruyuan.jiangzh.iot.user.domain.entity.SecurityUser;
import com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.UserRepository;
import com.ruyuan.jiangzh.iot.user.infrastructure.auth.HeaderAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class HeaderAuthenticationProvider implements AuthenticationProvider {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserRepository userRepository;
    public HeaderAuthenticationProvider(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /*
        验证对象的权限
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SecurityUser userSecurity = null;
        if(authentication.getPrincipal() instanceof SecurityUser){
            userSecurity =  (SecurityUser)authentication.getPrincipal();
        }

        UserEntity userEntity = userRepository.describeUserByName(userSecurity.getUsername());
        SecurityUser result = null;
        if(userEntity != null){
            result = new SecurityUser(userEntity);
        }

        logger.info("authenticate userEntity:[{}] ,result : [{}],  userSecurity:[{}]", userEntity, result , userSecurity);

        return new HeaderAuthenticationToken(result);
    }

    /*
        用来判断是否应该这个provider处理请求
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return (HeaderAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
