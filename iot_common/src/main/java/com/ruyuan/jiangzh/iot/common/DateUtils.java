package com.ruyuan.jiangzh.iot.common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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


    public static void main(String[] args) {
        long timestamp = System.currentTimeMillis();
        System.out.println("timestamp = " + timestamp);
        Date dateByTimestamp = getDateByTimestamp(timestamp);
        System.out.println("dateByTimestamp = " + dateByTimestamp);
        long timestampByDate = getTimestampByDate(dateByTimestamp);
        System.out.println("timestampByDate = " + timestampByDate);
    }

    /*
        通过时间戳获取date格式
     */
    public static Date getDateByTimestamp(long timestamp){
        return new Date(timestamp);
    }

    /*
       通过date格式获取时间戳
    */
    public static long getTimestampByDate(Date date){
        return date.getTime();
    }

}
