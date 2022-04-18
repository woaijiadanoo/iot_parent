package com.ruyuan.jiangzh.iot.user.domain.vo;

public enum AuthorityRole {

    SYS_ADMIN(0),
    TENANT_ADMIN(1),
    USER(2);

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

}
