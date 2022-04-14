package com.ruyuan.jiangzh.iot.base.uuid;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.UUID;

public interface EntityId extends Serializable {

    // 代表 0
    UUID NULL_ID = UUID.fromString("13814000-1dd2-11b2-8080-808080808080");
    // 获取业务数据实体的ID信息
    UUID getId();
    // 获取业务数据提示的类型信息
    EntityType getEntityType();

    @JsonIgnore
    default boolean isNullId(){
        return NULL_ID.equals(getId());
    }

}
