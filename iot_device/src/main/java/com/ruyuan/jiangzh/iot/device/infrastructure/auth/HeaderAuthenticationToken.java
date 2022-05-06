package com.ruyuan.jiangzh.iot.device.infrastructure.auth;

import com.ruyuan.jiangzh.iot.base.security.IoTSecurityUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class HeaderAuthenticationToken extends AbstractAuthenticationToken {

    private IoTSecurityUser userSecurity;

    public HeaderAuthenticationToken(IoTSecurityUser userSecurity) {
        super(userSecurity.getAuthorities());
        this.userSecurity = userSecurity;
    }

    public HeaderAuthenticationToken(String username) {
        super(IoTSecurityUser.getDefaultAuthorities());
        userSecurity = new IoTSecurityUser(username);
    }

    @Override
    public Object getCredentials() {
        return userSecurity;
    }

    @Override
    public Object getPrincipal() {
        return userSecurity;
    }
}
