package com.ruyuan.jiangzh.iot.base.uuid.device;

import com.ruyuan.jiangzh.iot.base.uuid.EntityType;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDBased;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.UUID;

public class ProductId extends UUIDBased {

    @JsonCreator
    public ProductId(UUID id){
        super(id);
    }

    @JsonIgnore
    public EntityType getEntityType(){
        return EntityType.PRODUCT;
    }

}
