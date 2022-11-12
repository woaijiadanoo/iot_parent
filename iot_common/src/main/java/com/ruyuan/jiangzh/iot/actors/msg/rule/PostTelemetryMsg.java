package com.ruyuan.jiangzh.iot.actors.msg.rule;

import com.ruyuan.jiangzh.iot.actors.msg.rule.vo.TsKvListProtoVO;

public class PostTelemetryMsg {

    private TsKvListProtoVO kvList;

    public TsKvListProtoVO getKvList() {
        return kvList;
    }

    public void setKvList(TsKvListProtoVO kvList) {
        this.kvList = kvList;
    }
}
