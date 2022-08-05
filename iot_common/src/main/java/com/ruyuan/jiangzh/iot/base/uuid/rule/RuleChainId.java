package com.ruyuan.jiangzh.iot.base.uuid.rule;

import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.EntityType;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDBased;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.UUID;

public class RuleChainId  extends UUIDBased implements EntityId {

    @JsonCreator
    public RuleChainId(UUID id){
        super(id);
    }

    @JsonIgnore
    public EntityType getEntityType(){
        return EntityType.RULE_CHAIN;
    }


}
