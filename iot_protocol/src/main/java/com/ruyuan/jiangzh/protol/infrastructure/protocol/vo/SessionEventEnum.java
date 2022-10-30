package com.ruyuan.jiangzh.protol.infrastructure.protocol.vo;

public enum SessionEventEnum {

    OPEN(0),CLOSE(1);

    private int sessionEventCode;

    SessionEventEnum(int sessionEventCode){
        this.sessionEventCode = sessionEventCode;
    }

    public int getSessionEventCode() {
        return sessionEventCode;
    }
}
