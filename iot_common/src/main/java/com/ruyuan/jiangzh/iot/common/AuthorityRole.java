package com.ruyuan.jiangzh.iot.common;

public enum AuthorityRole {

    SYS_ADMIN(0),
    TENANT_ADMIN(1),
    USER(2),
    DEFAULT_USER(3);

    private int code;
    AuthorityRole(int code){
        this.code =code;
    }

    public static AuthorityRole describeRoleByCode(Integer code){
        AuthorityRole authorityRole = null;
        if(code != null){
            for(AuthorityRole currentRole : AuthorityRole.values()){
                if(currentRole.code == code){
                    return currentRole;
                }
            }
        }
        return authorityRole;
    }

    public static AuthorityRole describeRoleByName(String name){
        AuthorityRole authorityRole = null;
        if(IoTStringUtils.isBlank(name)){
            for(AuthorityRole currentRole : AuthorityRole.values()){
                if(currentRole.name().equals(name)){
                    return currentRole;
                }
            }
        }
        return authorityRole;
    }

}
