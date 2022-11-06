package com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt;

import com.ruyuan.jiangzh.protol.infrastructure.protocol.ProtocolApiService;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.ProtocolContext;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt.common.MqttProtocolAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
@Component
public class MqttProtocolContext extends ProtocolContext {

    @Autowired
    private MqttProtocolAdaptor protocolAdaptor;

    @Value("${protocol.mqtt.max_payload_size}")
    private Integer maxPayloadSize;

    public void init(){}

    public Integer getMaxPayloadSize() {
        return maxPayloadSize;
    }

    public void setMaxPayloadSize(Integer maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
    }

    public MqttProtocolAdaptor getProtocolAdaptor() {
        return protocolAdaptor;
    }

    public void setProtocolAdaptor(MqttProtocolAdaptor protocolAdaptor) {
        this.protocolAdaptor = protocolAdaptor;
    }
}
