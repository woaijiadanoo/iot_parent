package com.ruyuan.jiangzh.protol.infrastructure.protocol.impl;

import com.ruyuan.jiangzh.iot.actors.RpcManager;
import com.ruyuan.jiangzh.iot.actors.msg.device.FromDeviceMsg;
import com.ruyuan.jiangzh.protol.infrastructure.acl.RpcManagerService;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.AbstractProtocolService;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.ProtocolApiService;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.common.FutureCallbackUtils;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.common.ProtocolServiceCallback;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.ProtocolApiReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.SessionEventMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthRespMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.vo.SessionInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
@Service
public class ProtocolServiceImpl extends AbstractProtocolService {

    @Autowired
    private ProtocolApiService protocolApiService;

    @Autowired
    private RpcManagerService rpcManagerService;

    @Override
    protected void doProcess(SessionInfoVO sessionInfo, SessionEventMsg sessionEventMsg, ProtocolServiceCallback<Void> callback) {

    }

    /*
            实际业务处理的封装
         */
    @Override
    protected void doProcess(DeviceAuthReqMsg msg, ProtocolServiceCallback<DeviceAuthRespMsg> callback) {
        // 实现callback的调用
        FutureCallbackUtils.withCallback(
                protocolApiService.handle(new ProtocolApiReqMsg(msg)),
                protocolApiReqMsg -> {
                    if(callback != null){
                        callback.onSuccess(protocolApiReqMsg.getDeviceAuthRespMsg());
                    }
                },
                getThrowableConsumer(callback), protocolCallbackExecutor
        );
    }

    @Override
    protected void doProcess(FromDeviceMsg msg) {
        rpcManagerService.onMsg(msg);
    }

    /*
        业务异常处理
     */
    private <T> Consumer<Throwable> getThrowableConsumer(ProtocolServiceCallback<DeviceAuthRespMsg> callback) {
        return e -> {
            if(callback != null){
                callback.onError(e);
            }
        };
    }
}
