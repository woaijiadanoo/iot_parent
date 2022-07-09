package com.ruyuan.jiangzh.iot.base.uuid.tenant;

import com.ruyuan.jiangzh.iot.base.uuid.EntityIdBase;
import com.ruyuan.jiangzh.iot.base.uuid.EntityType;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDBased;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.UUID;

public class TenantId extends UUIDBased {

    @JsonCreator
    public TenantId(UUID id){
        super(id);
    }

    @JsonIgnore
    public EntityType getEntityType(){
        return EntityType.TENANT;
    }

}
