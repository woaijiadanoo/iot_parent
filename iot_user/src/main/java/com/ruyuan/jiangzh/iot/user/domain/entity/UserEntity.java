package com.ruyuan.jiangzh.iot.user.domain.entity;

import com.ruyuan.jiangzh.iot.common.AuthorityRole;

import java.util.UUID;

public class UserEntity {

    private UUID tenantId;
    private UUID userId;
    private String username;
    private AuthorityRole authorityRole;

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
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
}
