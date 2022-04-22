package com.ruyuan.jiangzh.iot.user.domain.entity;

import com.ruyuan.jiangzh.iot.user.domain.vo.AuthorityRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserSecurity extends UserEntity{

    public UserSecurity(){}

    public UserSecurity(String username){
        this.setUsername(username);
        this.setAuthorityRole(AuthorityRole.DEFAULT_USER);
    }

    private Collection<GrantedAuthority> authorities;

    public static Collection<GrantedAuthority> getDefaultAuthorities() {
        Collection<GrantedAuthority> authorities = Stream.of(AuthorityRole.DEFAULT_USER)
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        return authorities;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        if(getAuthorityRole() != null){
            if(authorities == null){
                authorities = Stream.of(getAuthorityRole())
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .collect(Collectors.toList());
            }
        }
        return authorities;
    }
}
