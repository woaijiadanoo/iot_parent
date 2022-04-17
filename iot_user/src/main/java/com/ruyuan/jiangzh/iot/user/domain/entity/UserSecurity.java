package com.ruyuan.jiangzh.iot.user.domain.entity;

import com.ruyuan.jiangzh.iot.user.domain.vo.AuthorityRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserSecurity extends UserEntity{

    private AuthorityRole authorityRole;
    private Collection<GrantedAuthority> authorities;

    public AuthorityRole getAuthorityRole() {
        return authorityRole;
    }

    public void setAuthorityRole(AuthorityRole authorityRole) {
        this.authorityRole = authorityRole;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        if(authorityRole != null){
            if(authorities == null){
                authorities = Stream.of(authorityRole)
                        .map(role -> new SimpleGrantedAuthority(authorityRole.name()))
                        .collect(Collectors.toList());
            }
        }
        return authorities;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
