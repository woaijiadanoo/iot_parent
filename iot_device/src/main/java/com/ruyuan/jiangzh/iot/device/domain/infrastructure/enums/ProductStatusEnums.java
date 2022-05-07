package com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums;

public enum ProductStatusEnums {

    DRAFT(0), RELESE(1);

    private int code;

    ProductStatusEnums(int code){
        this.code = code;
    }

    public static ProductStatusEnums getByCode(int code){
        ProductStatusEnums enums = null;
        for(ProductStatusEnums e : values()){
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
