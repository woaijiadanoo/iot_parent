package com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt;

import com.ruyuan.jiangzh.protol.infrastructure.protocol.ProtocolContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
@Component
public class MqttProtocolContext extends ProtocolContext {

    @Value("${protocol.mqtt.max_payload_size}")
    private Integer maxPayloadSize;

    public Integer getMaxPayloadSize() {
        return maxPayloadSize;
    }

    public void setMaxPayloadSize(Integer maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
    }
}
