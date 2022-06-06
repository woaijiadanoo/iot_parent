package com.ruyuan.jiangzh.iot.device.infrastructure.auth;

import com.ruyuan.jiangzh.iot.base.security.IoTSecurityUser;
import com.ruyuan.jiangzh.service.dto.SecurityUserDTO;
import com.ruyuan.jiangzh.service.sdk.UserServiceAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class HeaderAuthenticationProvider implements AuthenticationProvider {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserServiceAPI userServiceAPI;
    public HeaderAuthenticationProvider(UserServiceAPI userServiceAPI){
        this.userServiceAPI = userServiceAPI;
    }

    /*
        验证对象的权限
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        IoTSecurityUser userSecurity = null;
        if(authentication.getPrincipal() instanceof IoTSecurityUser){
            userSecurity =  (IoTSecurityUser)authentication.getPrincipal();
        }

        SecurityUserDTO securityUserDTO = userServiceAPI.describeUserByName(userSecurity.getUsername());
        IoTSecurityUser result = null;
        if(securityUserDTO != null){
            result = new IoTSecurityUser(
                    securityUserDTO.getUserId(),
                    securityUserDTO.getTenantId(),
                    securityUserDTO.getAuthorityRole(),
                    securityUserDTO.getUsername(),
                    securityUserDTO.getEmail(),
                    securityUserDTO.getPhone()
            );
        }

        logger.info("authenticate userEntity:[{}] ,result : [{}],  userSecurity:[{}]", securityUserDTO, result , userSecurity);

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
