package com.ruyuan.jiangzh.protol.infrastructure.protocol;

import com.google.common.util.concurrent.ListenableFuture;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.ProtocolApiReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.ProtocolApiRespMsg;

/**
 * @author jiangzheng
 * @version 1.0
 * @description: 协议处理接口
 */
public interface ProtocolApiService {

    // 统一的协议处理机制
    ListenableFuture<ProtocolApiRespMsg> handle(ProtocolApiReqMsg requestMsg);

}
