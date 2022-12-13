package com.ruyuan.jiangzh.service.dto;

public enum ThingModelType {

    SHADOW(0), PROPERTIES(1), EVENTS(2), SERVICES(3);

    private int code;

    ThingModelType(int code) {
        this.code = code;
    }

    public static ThingModelType getByCode(int code){
        for(ThingModelType type : values()){
            if(type.getCode() == code){
                return type;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

}
