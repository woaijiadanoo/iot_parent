package com.ruyuan.jiangzh.iot.user.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.EntityType;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDBased;

import java.util.UUID;

public class UserId extends UUIDBased implements EntityId {

    public UserId(UUID id){
        super(id);
    }

    @JsonIgnore
    @Override
    public EntityType getEntityType() {
        return EntityType.USER;
    }
}
