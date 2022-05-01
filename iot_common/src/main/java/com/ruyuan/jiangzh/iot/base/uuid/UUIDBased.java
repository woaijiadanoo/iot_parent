package com.ruyuan.jiangzh.iot.base.uuid;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import java.io.Serializable;
import java.util.UUID;

public abstract class UUIDBased implements Serializable {

    private final UUID uuid;

    public UUIDBased(){
        // 生成了一个UUID
        this(UUIDHelper.genUuid());
    }

    public UUIDBased(UUID uuid){
        super();
        this.uuid = uuid;
    }

    public UUID getUuid(){
        return uuid;
    }

    @Override
    public int hashCode() {
        final  int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){ return true; }
        if(obj == null){ return false; }
        if(getClass() != obj.getClass()){ return  false;}
        UUIDBased based = (UUIDBased)obj;
        if(uuid == null){
            if(based.uuid != null){
                return false;
            }
        }else if(!uuid.equals(based.uuid)){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
