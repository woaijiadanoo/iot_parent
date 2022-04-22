package com.ruyuan.jiangzh.iot.user.temp;

import com.ruyuan.jiangzh.iot.user.domain.entity.UserSecurity;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class HeaderAuthenticationToken extends AbstractAuthenticationToken {

    private UserSecurity userSecurity;

    public HeaderAuthenticationToken(UserSecurity userSecurity) {
        super(userSecurity.getAuthorities());
        this.userSecurity = userSecurity;
    }

    public HeaderAuthenticationToken(String username) {
        super(UserSecurity.getDefaultAuthorities());
        userSecurity = new UserSecurity(username);
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
