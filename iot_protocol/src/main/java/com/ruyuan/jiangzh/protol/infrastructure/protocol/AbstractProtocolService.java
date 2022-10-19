package com.ruyuan.jiangzh.protol.infrastructure.protocol;

import com.ruyuan.jiangzh.protol.infrastructure.protocol.common.ProtocolServiceCallback;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthRespMsg;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public abstract class AbstractProtocolService implements ProtocolService{

    protected ExecutorService protocolCallbackExecutor;

    @Override
    public void process(DeviceAuthReqMsg msg, ProtocolServiceCallback<DeviceAuthRespMsg> callback) {
        // 可能有一些其他内容要做

        // 调用子类的具体业务实现
        doProcess(msg, callback);
    }

    // 具体的设备鉴权实现，交给子类处理
    protected abstract void doProcess(DeviceAuthReqMsg msg, ProtocolServiceCallback<DeviceAuthRespMsg> callback);

    public void init(){
        this.protocolCallbackExecutor = Executors.newWorkStealingPool(20);
    }

}
