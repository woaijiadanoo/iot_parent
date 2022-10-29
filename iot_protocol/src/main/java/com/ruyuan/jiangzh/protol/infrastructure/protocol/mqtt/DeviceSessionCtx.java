package com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt;

import io.netty.channel.ChannelHandlerContext;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class DeviceSessionCtx extends MqttDeviceAwareSessionContext{

    private ChannelHandlerContext channel;

    private AtomicInteger msgIdSeq = new AtomicInteger();

    public DeviceSessionCtx(UUID sessionId) {
        super(sessionId);
    }

    public ChannelHandlerContext getChannel() {
        return channel;
    }

    public void setChannel(ChannelHandlerContext channel) {
        this.channel = channel;
    }

    @Override
    public int nextMsgId() {
        return msgIdSeq.incrementAndGet();
    }
}
