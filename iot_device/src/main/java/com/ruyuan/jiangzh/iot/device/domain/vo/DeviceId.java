package com.ruyuan.jiangzh.iot.device.domain.vo;

import com.ruyuan.jiangzh.iot.base.uuid.EntityType;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDBased;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.UUID;

public class DeviceId  extends UUIDBased {

    @JsonCreator
    public DeviceId(UUID id){
        super(id);
    }

    @JsonIgnore
    public EntityType getEntityType(){
        return EntityType.DEVICE;
    }

}