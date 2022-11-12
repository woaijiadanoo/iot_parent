package com.ruyuan.jiangzh.protol.infrastructure.protocol.impl;

import com.google.gson.Gson;
import com.ruyuan.jiangzh.iot.actors.msg.device.FromDeviceMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.ToDeviceSessionEventMsg;
import com.ruyuan.jiangzh.iot.actors.msg.rule.TransportToRuleEngineActorMsg;
import com.ruyuan.jiangzh.iot.actors.msg.rule.TransportToRuleEngineActorMsgWrapper;
import com.ruyuan.jiangzh.protol.infrastructure.acl.RpcManagerService;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.AbstractProtocolService;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.ProtocolApiService;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.common.FutureCallbackUtils;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.common.ProtocolServiceCallback;
import com.ruyuan.jiangzh.iot.actors.msg.rule.PostTelemetryMsg;
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
        // 封装待传递的对象
        ToDeviceSessionEventMsg toDeviceSessionEventMsg = new ToDeviceSessionEventMsg(
                sessionInfo.getDeviceId(),sessionInfo.getTenantId(),sessionEventMsg.getSessionEvent().getSessionEventCode());

        if(getSessionMetaData(sessionInfo) != null){
            toDeviceSessionEventMsg.setLastActivityTimestamp(getSessionMetaData(sessionInfo).getLastActivityTime());
        }else{
            // 如果最后记录为空， 就以当前时间为准
            toDeviceSessionEventMsg.setLastActivityTimestamp(System.currentTimeMillis());
        }

        // 调用RPCManager
        rpcManagerService.broadcast(toDeviceSessionEventMsg);
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


    /*
    处理tag数据上报
    ProtocolServiceImpl postTelemetryMsg =
    {"kvList":
        {
            "reviceTime":1668094392526,
            "kvs":
                [
                    {"key":"key","type":"STRING_V","stringValue":"testKey"},
                    {"key":"value","type":"STRING_V","stringValue":"testValue"}
               ]
         }


          {"kvList":{"reviceTime":1668094514000,"kvs":[{"key":"key","type":"STRING_V","stringValue":"testKey"},{"key":"value","type":"LONG_V","longValue":38}]}}
    }
 */
    @Override
    protected void doProcess(SessionInfoVO sessionInfo, PostTelemetryMsg postTelemetryMsg, ProtocolServiceCallback<Void> callback) {
        TransportToRuleEngineActorMsg msg = new TransportToRuleEngineActorMsg.NewBuilder().setPostTelemetryMsg(postTelemetryMsg).build();
        forwardToRuleEngineActor(sessionInfo, msg, callback);
    }

    private void forwardToRuleEngineActor(
            SessionInfoVO sessionInfo, TransportToRuleEngineActorMsg msg, ProtocolServiceCallback<Void> callback) {
        // 转换成TransportWrapper对象
        TransportToRuleEngineActorMsgWrapper wrapper =
                new TransportToRuleEngineActorMsgWrapper(sessionInfo.getTenantId(),sessionInfo.getDeviceId(), msg);
        // RPC投递消息
        rpcManagerService.onMsg(wrapper);

        // 返回成功
        if(callback != null){
            callback.onSuccess(null);
        }

    }

}
