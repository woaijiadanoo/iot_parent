package com.ruyuan.jiangzh.protol.infrastructure.protocol.impl;

import com.ruyuan.jiangzh.protol.infrastructure.protocol.AbstractProtocolService;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.ProtocolApiService;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.common.FutureCallbackUtils;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.common.ProtocolServiceCallback;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.ProtocolApiReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthRespMsg;
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
