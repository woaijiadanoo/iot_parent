package com.ruyuan.jiangzh.iot.base.security;

import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.UserId;
import com.ruyuan.jiangzh.iot.common.AuthorityRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IoTSecurityUser {

    private TenantId tenantId;
    private UserId userId;
    private String username;
    private AuthorityRole authorityRole;
    private String email;
    private String phone;


    private Collection<GrantedAuthority> authorities;


    public IoTSecurityUser(String username){
        this.setUsername(username);
        this.setAuthorityRole(AuthorityRole.DEFAULT_USER);
    }

    public IoTSecurityUser(
            UserId userId, TenantId tenantId,AuthorityRole authorityRole,
            String username,String email,String phone){
        this.userId = userId;
        this.tenantId = tenantId;
        this.authorityRole = authorityRole;
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

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

    public TenantId getTenantId() {
        return tenantId;
    }

    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AuthorityRole getAuthorityRole() {
        return authorityRole;
    }

    public void setAuthorityRole(AuthorityRole authorityRole) {
        this.authorityRole = authorityRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
