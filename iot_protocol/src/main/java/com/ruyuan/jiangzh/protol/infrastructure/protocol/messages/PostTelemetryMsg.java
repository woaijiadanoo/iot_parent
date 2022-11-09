package com.ruyuan.jiangzh.protol.infrastructure.protocol.messages;

import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.vo.TsKvListProtoVO;

public class PostTelemetryMsg {

    private TsKvListProtoVO kvList;

    public TsKvListProtoVO getKvList() {
        return kvList;
    }

    public void setKvList(TsKvListProtoVO kvList) {
        this.kvList = kvList;
    }
}
