package com.ruyuan.jiangzh.protol.infrastructure.protocol;

import java.util.UUID;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public interface SessionContext {

    UUID getSessionId();

    int nextMsgId();

}
