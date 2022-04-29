package com.ruyuan.jiangzh.iot.base.uuid;

import java.io.Serializable;
import java.util.Objects;

public abstract class CreateTimeIdBase<I extends UUIDBased> extends EntityIdBase implements Serializable {

    private long createTime;

    public CreateTimeIdBase(){}

    public CreateTimeIdBase(I id){ super(id); }

    public long getCreateTime() {
        return UUIDHelper.parseTimestampByUUID(getId().getUuid());
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateTimeIdBase)) return false;
        if (!super.equals(o)) return false;
        CreateTimeIdBase<?> that = (CreateTimeIdBase<?>) o;
        return getCreateTime() == that.getCreateTime();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCreateTime());
    }

    @Override
    public String toString() {
        return "TimestampIdBase{" +
                "createTime=" + createTime +
                '}';
    }
}
