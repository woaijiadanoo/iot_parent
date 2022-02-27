package com.ruyuan.jiangzh.iot.common;

import java.util.UUID;

public class IoTStringUtils {

    /*
        1111-1111-111
     */
    public static String requestId(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
