package com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt.common;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.common.JsonConverter;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.exceptions.AdaptorException;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt.DeviceSessionCtx;
import com.ruyuan.jiangzh.iot.actors.msg.rule.PostTelemetryMsg;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.UUID;

@Service
public class JsonProtocolAdaptor implements MqttProtocolAdaptor{

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public PostTelemetryMsg convertToPostTelemetryMsg(DeviceSessionCtx ctx, MqttPublishMessage mqttMsg) throws AdaptorException {
        // 获取payload
        String payload = validatePayload(ctx.getSessionId(), mqttMsg.payload());

        // 将payload转换成json
        try {
            return JsonConverter.convertToTelemetryMsg(new JsonParser().parse(payload));
        } catch (IllegalStateException | JsonSyntaxException ex) {
            throw new AdaptorException(ex);
        }
    }

    private String validatePayload(UUID sessionId, ByteBuf payloadData) throws AdaptorException {
        try {
            String payload = payloadData.toString(UTF8);
            if(payload == null){
                throw new AdaptorException(new IllegalArgumentException("Payload is empty!"));
            }
            return payload;
        } finally {
            payloadData.release();
        }
    }


}
