package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common;

import com.ruyuan.jiangzh.iot.base.uuid.EntityId;

import java.io.Serializable;

public final class RuleEngineMsg implements Serializable {
    private final String data;

    private final String type;

    private final EntityId originator;

    private final RuleEngineMsgMetaData metaData;

    public RuleEngineMsg(String data, String type, EntityId originator, RuleEngineMsgMetaData metaData) {
        this.data = data;
        this.type = type;
        this.originator = originator;
        this.metaData = metaData;
    }

    public String getData() {
        return data;
    }

    public String getType() {
        return type;
    }

    public EntityId getOriginator() {
        return originator;
    }

    public RuleEngineMsgMetaData getMetaData() {
        return metaData;
    }
}
