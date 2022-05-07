package com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums;

public enum DeviceTypeEnums {

    DEVICE(0), EDGE(1), EDGE_SUB(2);

    private int code;

    DeviceTypeEnums(int code){
        this.code = code;
    }

    public static DeviceTypeEnums getByCode(int code){
        DeviceTypeEnums enums = null;
        for(DeviceTypeEnums e : values()){
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
