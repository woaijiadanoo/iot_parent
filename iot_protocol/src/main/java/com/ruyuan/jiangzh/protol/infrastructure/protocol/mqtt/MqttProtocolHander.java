package com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class MqttProtocolHander extends ChannelInboundHandlerAdapter
        implements GenericFutureListener<Future<? super Void>>, SessionMsgListener {

    private final MqttProtocolContext context;

    public MqttProtocolHander(MqttProtocolContext context){
        this.context = context;
    }

    @Override
    public void operationComplete(Future<? super Void> future) throws Exception {

    }
}
