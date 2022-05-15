package com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums;

public enum DeviceStatusEnums {

    NOT_ACTIVE(0), ACTIVED(1), OFFLINE(2), ONLINE(3);

    private int code;

    DeviceStatusEnums(int code){
        this.code = code;
    }

    public static DeviceStatusEnums getByCode(int code){
        DeviceStatusEnums enums = null;
        for(DeviceStatusEnums e : values()){
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
