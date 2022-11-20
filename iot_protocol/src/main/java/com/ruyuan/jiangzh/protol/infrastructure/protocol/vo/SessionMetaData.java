package com.ruyuan.jiangzh.protol.infrastructure.protocol.vo;

import com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt.SessionMsgListener;

public class SessionMetaData {

    private final SessionInfoVO sessionInfo;

    /*
        现阶段 基本等同于 = MqttProtocolHandler的引用
     */
    private SessionMsgListener sessionMsgListener;

    private volatile long lastActivityTime;

    public SessionMetaData(SessionInfoVO sessionInfo){
        this.sessionInfo = sessionInfo;
    }

    public SessionMetaData(SessionInfoVO sessionInfo, SessionMsgListener listener){
        this.sessionInfo = sessionInfo;
        this.sessionMsgListener = listener;
    }

    public long getLastActivityTime() {
        return lastActivityTime;
    }

    public void updateLastActivityTime(){
        this.lastActivityTime = System.currentTimeMillis();
    }

    public SessionMsgListener getSessionMsgListener() {
        return sessionMsgListener;
    }
}
