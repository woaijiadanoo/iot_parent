package com.ruyuan.jiangzh.iot.base.uuid;

import java.io.Serializable;
import java.util.Objects;

public abstract class EntityIdBase<I extends UUIDBased> implements Serializable {

    private I id;

    public EntityIdBase(){}

    public EntityIdBase(I id){
        super();
        this.id = id;
    }

    public void setId(I id) {
        this.id = id;
    }

    public I getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if(o == null){
            return false;
        }
        if(getClass() != o.getClass()){
            return  false;
        }
        EntityIdBase other = (EntityIdBase)o;
        if(id == null){
            if(other.id != null){
                return false;
            }
        } else if(!id.equals(other.getId())){
            return  false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}
