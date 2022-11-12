package com.ruyuan.jiangzh.iot.actors.msg.rule.vo;

import java.util.List;

public class TsKvListProtoVO {

    private long reviceTime;

    private List<KeyValueProtoVO> kvs;

    public long getReviceTime() {
        return reviceTime;
    }

    public void setReviceTime(long reviceTime) {
        this.reviceTime = reviceTime;
    }

    public List<KeyValueProtoVO> getKvs() {
        return kvs;
    }

    public void setKvs(List<KeyValueProtoVO> kvs) {
        this.kvs = kvs;
    }
}
