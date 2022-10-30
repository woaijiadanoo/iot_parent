package com.ruyuan.jiangzh.protol.infrastructure.protocol.messages;

import com.ruyuan.jiangzh.protol.infrastructure.protocol.vo.SessionEventEnum;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class SessionEventMsg {

    private final SessionEventEnum sessionEvent;

    public SessionEventMsg(SessionEventEnum sessionEvent){
        this.sessionEvent = sessionEvent;
    }

    public SessionEventEnum getSessionEvent() {
        return sessionEvent;
    }
}
