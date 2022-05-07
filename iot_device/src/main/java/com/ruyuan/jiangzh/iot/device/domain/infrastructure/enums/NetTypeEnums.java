package com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums;

public enum NetTypeEnums {

    WIFI(0), NB_IOT(1);

    private int code;

    NetTypeEnums(int code){
        this.code = code;
    }

    public static NetTypeEnums getByCode(int code){
        NetTypeEnums enums = null;
        for(NetTypeEnums e : values()){
            if(e.getCode() == code){
                enums = e;
                break;
            }
        }
        return enums;
    }

    public int getCode() {
        return code;
    }
}
