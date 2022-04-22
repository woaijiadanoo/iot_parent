package com.ruyuan.jiangzh.iot.user.temp;

import com.ruyuan.jiangzh.iot.user.domain.entity.SecurityUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class HeaderAuthenticationToken extends AbstractAuthenticationToken {

    private SecurityUser userSecurity;

    public HeaderAuthenticationToken(SecurityUser userSecurity) {
        super(userSecurity.getAuthorities());
        this.userSecurity = userSecurity;
    }

    public HeaderAuthenticationToken(String username) {
        super(SecurityUser.getDefaultAuthorities());
        userSecurity = new SecurityUser(username);
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
