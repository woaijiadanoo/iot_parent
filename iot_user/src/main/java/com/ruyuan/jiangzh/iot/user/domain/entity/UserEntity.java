package com.ruyuan.jiangzh.iot.user.domain.entity;

import com.ruyuan.jiangzh.iot.user.domain.vo.AuthorityRole;

public class UserEntity {

    private String username;
    private AuthorityRole authorityRole;

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
