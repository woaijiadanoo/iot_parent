package com.ruyuan.jiangzh.iot.user.controller;

import java.io.Serializable;

public class UserDTO implements Serializable {

    private UserId userId;
    private String username;

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
}
