package com.ruyuan.jiangzh.protol.infrastructure.protocol;

import com.google.common.collect.Maps;
import com.ruyuan.jiangzh.iot.actors.msg.device.FromDeviceMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.SubscribeToAttrUpdateMsg;
import com.ruyuan.jiangzh.iot.base.uuid.EntityType;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.common.ProtocolRateLimits;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.common.ProtocolRateLimitsException;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.common.ProtocolServiceCallback;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.SessionEventMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthRespMsg;
import com.ruyuan.jiangzh.iot.actors.msg.rule.PostTelemetryMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt.SessionMsgListener;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.vo.SessionEventEnum;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.vo.SessionInfoVO;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.vo.SessionMetaData;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public abstract class AbstractProtocolService implements ProtocolService{

    protected ExecutorService protocolCallbackExecutor;

    private ConcurrentMap<UUID, SessionMetaData> sessions = Maps.newConcurrentMap();

    @Override
    public void process(DeviceAuthReqMsg msg, ProtocolServiceCallback<DeviceAuthRespMsg> callback) {
        // 可能有一些其他内容要做

        // 调用子类的具体业务实现
        doProcess(msg, callback);
    }

    @Override
    public void process(FromDeviceMsg msg) {
        // 可能有一些其他内容要做

        // 调用子类的具体业务实现
        doProcess(msg);
    }

    @Override
    public void process(SessionInfoVO sessionInfo, SessionEventMsg sessionEventMsg, ProtocolServiceCallback<Void> callback) {
        // 加入限流操作
        if(checkLimits(sessionInfo, sessionEventMsg, callback)){
            reportActivityInternal(sessionInfo);
            doProcess(sessionInfo, sessionEventMsg, callback);
        }
    }

    @Override
    public void process(SessionInfoVO sessionInfo, PostTelemetryMsg postTelemetryMsg, ProtocolServiceCallback<Void> callback) {
        // 加入限流操作
        if(checkLimits(sessionInfo, postTelemetryMsg, callback)){
            reportActivityInternal(sessionInfo);
            doProcess(sessionInfo, postTelemetryMsg, callback);
        }
    }

    @Override
    public void process(SessionInfoVO sessionInfo, SubscribeToAttrUpdateMsg attrUpdateMsg, ProtocolServiceCallback<Void> callback) {
        // 加入限流操作
        if(checkLimits(sessionInfo, attrUpdateMsg, callback)){
            reportActivityInternal(sessionInfo);
            doProcess(sessionInfo, attrUpdateMsg, callback);
        }
    }

    protected abstract void doProcess(SessionInfoVO sessionInfo, SubscribeToAttrUpdateMsg attrUpdateMsg, ProtocolServiceCallback<Void> callback);

    protected abstract void doProcess(SessionInfoVO sessionInfo, PostTelemetryMsg postTelemetryMsg, ProtocolServiceCallback<Void> callback);

    // 设备事件的统一处理
    protected abstract void doProcess(SessionInfoVO sessionInfo, SessionEventMsg sessionEventMsg, ProtocolServiceCallback<Void> callback);

    // 具体的设备鉴权实现，交给子类处理
    protected abstract void doProcess(DeviceAuthReqMsg msg, ProtocolServiceCallback<DeviceAuthRespMsg> callback);

    protected abstract void doProcess(FromDeviceMsg msg);

    public void init(){
        this.protocolCallbackExecutor = Executors.newWorkStealingPool(20);
    }


    /*
        限流表达式： 容量:窗口长度,容量:窗口长度
     */
    // 是否开启限流
    @Value("${protocol.rate.limits.enabled}")
    private boolean rateLimitEnabled;
    // Tenant的限流方案
    @Value("${protocol.rate.limits.tenant.config}")
    private String perTenantsLimitsConf;
    // Device的限流方案
    @Value("${protocol.rate.limits.device.config}")
    private String perDevicesLimitsConf;
    // 存储TenantId对应的令牌桶
    private ConcurrentMap<TenantId, ProtocolRateLimits> perTenantLimits = Maps.newConcurrentMap();
    // 存储DeviceId对应的令牌桶
    private ConcurrentMap<DeviceId, ProtocolRateLimits> perDeviceLimits = Maps.newConcurrentMap();

    @Override
    public boolean checkLimits(SessionInfoVO sessionInfo, Object msg, ProtocolServiceCallback<Void> callback) {
        if(!rateLimitEnabled){
            return true;
        }

        /*
            按照TenantId来进行限流
         */
        TenantId tenantId = sessionInfo.getTenantId();
        ProtocolRateLimits tenantRateLimit = perTenantLimits.computeIfAbsent(tenantId, id -> new ProtocolRateLimits(perTenantsLimitsConf));
        if(!tenantRateLimit.tryConsume()){
            if(callback != null){
                callback.onError(new ProtocolRateLimitsException(EntityType.TENANT));
            }
            return false;
        }

        /*
            按照DeviceId来进行限流
         */
        DeviceId deviceId = sessionInfo.getDeviceId();
        ProtocolRateLimits deviceRateLimit = perDeviceLimits.computeIfAbsent(deviceId, id -> new ProtocolRateLimits(perDevicesLimitsConf));
        if(!deviceRateLimit.tryConsume()){
            if(callback != null){
                callback.onError(new ProtocolRateLimitsException(EntityType.DEVICE));
            }
            return false;
        }

        return true;
    }


    public static SessionEventMsg getSessionEventMsg(SessionEventEnum sessionEventEnum){
        /*
            1、包括了事件的类型  open , close
            2、同步和异步
            3、额外的payload
         */
        return new SessionEventMsg(sessionEventEnum);
    }


    private SessionMetaData reportActivityInternal(SessionInfoVO sessionInfo){
        UUID sessionId = sessionInfo.getSessionId();
        SessionMetaData sessionMetaData = sessions.get(sessionId);
        if(sessionMetaData != null){
            sessionMetaData.updateLastActivityTime();
        }else{
            sessionMetaData = new SessionMetaData(sessionInfo);
            sessions.put(sessionId, sessionMetaData);
        }
        return sessionMetaData;
    }

    @Override
    public void deleteSessionMetaData(SessionInfoVO sessionInfo){
        sessions.remove(sessionInfo.getSessionId());
    }

    protected SessionMetaData getSessionMetaData(SessionInfoVO sessionInfo){
        return sessions.get(sessionInfo);
    }


    @Override
    public void registerSession(SessionInfoVO sessionInfo, SessionMsgListener listener) {
        sessions.putIfAbsent(sessionInfo.getSessionId(), new SessionMetaData(sessionInfo, listener));
    }


}
