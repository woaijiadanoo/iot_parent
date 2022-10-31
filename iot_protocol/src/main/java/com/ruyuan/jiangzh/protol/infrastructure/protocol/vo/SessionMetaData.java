package com.ruyuan.jiangzh.protol.infrastructure.protocol.vo;

public class SessionMetaData {

    private final SessionInfoVO sessionInfo;

    private volatile long lastActivityTime;

    public SessionMetaData(SessionInfoVO sessionInfo){
        this.sessionInfo = sessionInfo;
    }

    public long getLastActivityTime() {
        return lastActivityTime;
    }

    public void updateLastActivityTime(){
        this.lastActivityTime = System.currentTimeMillis();
    }

}
