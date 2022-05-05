package com.ruyuan.jiangzh.iot.common;

import java.util.UUID;

public class IoTStringUtils {

    /*
        1111-1111-111
     */
    public static String requestId(){
        return ThreadLocalUtils.getRequestId();
    }

    // 生成requestId
    public static String genRequestId(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static boolean isBlank(String str){
        if(str != null && str.trim().length()>0){
            return false;
        }
        return true;
    }

    // toUUID就是将字符串转换为UUID
    public static UUID toUUID(String uuidStr){
        return UUID.fromString(uuidStr);
    }

}
