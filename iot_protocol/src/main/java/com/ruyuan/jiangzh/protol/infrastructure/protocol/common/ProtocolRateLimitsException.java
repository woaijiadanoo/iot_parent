package com.ruyuan.jiangzh.protol.infrastructure.protocol.common;

import com.ruyuan.jiangzh.iot.base.uuid.EntityType;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class ProtocolRateLimitsException extends RuntimeException{

    private final EntityType entityType;

    public ProtocolRateLimitsException(EntityType entityType){
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
