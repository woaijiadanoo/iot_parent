package com.ruyuan.jiangzh.iot.common;

import java.util.Random;
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

    public static boolean isNotBlank(String str){
        return !isBlank(str);
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

    public static String getRandomString(int length){
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<length; i++){
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        return sb.toString();
    }

}
