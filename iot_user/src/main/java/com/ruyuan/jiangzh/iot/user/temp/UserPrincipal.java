package com.ruyuan.jiangzh.iot.user.temp;

import java.io.Serializable;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class UserPrincipal implements Serializable {

    private final Type type;
    private final String value;

    public UserPrincipal(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public enum Type {
        USER_NAME,
        PUBLIC_ID
    }

}
