package com.ruyuan.jiangzh.iot.base.enums;

public enum CommonEnums {
    USERNAME_HEADER("ruyuan_name");

    private String name;

    CommonEnums(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
