package com.ruyuan.jiangzh.iot.base.uuid;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import java.util.UUID;

public class UUIDHelper {
    // 生成一个带时间戳的UUID
    public static UUID genUuid(){
        return Uuids.timeBased();
    }
    // 根据UUID获取对应的时间戳信息
    public static long parseTimestampByUUID(UUID id){
        return Uuids.unixTimestamp(id);
    }

    // 将时间戳UUID转换为数据ID
    // ed86f3d0-add0-11ec-aba1-a7129eabce51
    public static String fromTimeUUID(UUID id){
        if(id.version() != 1){
            throw  new IllegalArgumentException("this is illegal uuid");
        }
        
        // c09b8330-add1-11ec-8dae-c1b3f8aff420
        String t = id.toString();
        // 1ecadd1c09b83308daec1b3f8aff420
        String result = t.substring(15,18) 
                        + t.substring(9,13) 
                        + t.substring(0,8)
                        + t.substring(19,23)
                        + t.substring(24);
        return result;
    }

    // 将数据Id字符串转换为UUID
    public static UUID fromStringId(String dataId){
        String uuidStr = dataId.substring(7,15) + "-"
                + dataId.substring(3,7) + "-1"
                + dataId.substring(0,3) + "-"
                + dataId.substring(15,19) + "-"
                + dataId.substring(19);

        UUID result = UUID.fromString(uuidStr);
        return result;
    }

    public static void main(String[] args) {
        UUID id = UUIDHelper.genUuid();
        System.out.println("id = " + id);

        long time = UUIDHelper.parseTimestampByUUID(id);
        System.out.println("time = " + time);

        String dataId = UUIDHelper.fromTimeUUID(id);
        System.out.println("dataId = " + dataId);

        // 7a24c410-add2-11ec-8b5e-63d0e3872bef
        // 7a24c410-add2-11ec-8b5e-63d0e3872bef
        UUID result = UUIDHelper.fromStringId(dataId);
        System.out.println("result = " + result);
    }

}
