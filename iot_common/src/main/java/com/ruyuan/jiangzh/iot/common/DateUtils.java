package com.ruyuan.jiangzh.iot.common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtils {

    private DateUtils(){}

    /*
        将千毫秒时间戳转换为LocalDateTime
     */
    public static LocalDateTime getLocalDateTimebyTimestamp(long timestamp){
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    /*
        将LocalDateTime转换为千毫秒时间戳
     */
    public static long getTimestampByLocalDateTime(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return instant.toEpochMilli();
    }

}
