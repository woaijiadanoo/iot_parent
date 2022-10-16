package com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class MqttProtoolServiceInitializer extends ChannelInitializer<SocketChannel> {

    private final MqttProtocolContext context;

    public MqttProtoolServiceInitializer(MqttProtocolContext context){
        this.context = context;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // MQTT进行处理
        pipeline.addLast("decoder", new MqttDecoder(context.getMaxPayloadSize()));
        pipeline.addLast("encoder", MqttEncoder.INSTANCE);

        // 初始化context的内容
        context.init();

        MqttProtocolHander hander = new MqttProtocolHander(context);

        pipeline.addLast(hander);

        ch.closeFuture().addListener(hander);

    }
}
