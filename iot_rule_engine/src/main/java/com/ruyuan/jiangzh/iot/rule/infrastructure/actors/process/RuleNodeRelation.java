package com.ruyuan.jiangzh.iot.rule.infrastructure.actors.process;

import com.ruyuan.jiangzh.iot.base.uuid.EntityId;

public class RuleNodeRelation {

    private final EntityId in;
    private final EntityId out;
    private final String type;

    public RuleNodeRelation(EntityId in, EntityId out,String type){
        this.in = in;
        this.out  = out;
        this.type = type;
    }

    public EntityId getIn() {
        return in;
    }

    public EntityId getOut() {
        return out;
    }

    public String getType() {
        return type;
    }
}
