package com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt.common;

import com.ruyuan.jiangzh.protol.infrastructure.protocol.exceptions.AdaptorException;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt.DeviceSessionCtx;
import com.ruyuan.jiangzh.iot.actors.msg.rule.PostTelemetryMsg;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

public interface MqttProtocolAdaptor {

    /*
        将 MqttPublishMessage -> 转换成 PostTelemetryMsg
     */
    PostTelemetryMsg convertToPostTelemetryMsg(DeviceSessionCtx ctx, MqttPublishMessage mqttMsg) throws AdaptorException;

}
