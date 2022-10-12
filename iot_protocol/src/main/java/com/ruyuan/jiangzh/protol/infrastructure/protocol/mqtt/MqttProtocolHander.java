package com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class MqttProtocolHander extends ChannelInboundHandlerAdapter
        implements GenericFutureListener<Future<? super Void>>, SessionMsgListener {

    private final MqttProtocolContext context;

    private volatile InetSocketAddress address;

    public MqttProtocolHander(MqttProtocolContext context){
        this.context = context;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead 被执行到了 ");
        if(msg instanceof MqttMessage){
            // 这里就可以处理MQTT的消息了
            processMqttMsg(ctx, (MqttMessage) msg);
        }else{
            ctx.close();
        }
    }

    private void processMqttMsg(ChannelHandlerContext ctx, MqttMessage msg) {
        address = (InetSocketAddress)ctx.channel().remoteAddress();
        if(msg.fixedHeader() == null){
            processDisConnect(ctx);
            return;
        }

        switch (msg.fixedHeader().messageType()) {
            // 处理连接请求
            case CONNECT:
                System.out.println(" 这是一个connect请求 ");
                processConnect(ctx, msg);
                break;
            case DISCONNECT:
                System.out.println(" 这是一个disConnect请求 ");
            default:
                break;
        }

    }

    // 处理连接请求
    private void processConnect(ChannelHandlerContext ctx, MqttMessage msg) {
        // 鉴权，是否有权限进行连接

        // 返回connectAck
        ctx.writeAndFlush(createMqttConnAckMsg(MqttConnectReturnCode.CONNECTION_ACCEPTED));
    }

    private MqttConnAckMessage createMqttConnAckMsg(MqttConnectReturnCode returnCode) {
        MqttFixedHeader mqttFixedHeader =
                new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0);

        MqttConnAckVariableHeader mqttConnAckVariableHeader =
                new MqttConnAckVariableHeader(returnCode, true);

        return new MqttConnAckMessage(mqttFixedHeader, mqttConnAckVariableHeader);
    }


    private void processDisConnect(ChannelHandlerContext ctx) {
        ctx.close();
    }

    @Override
    public void operationComplete(Future<? super Void> future) throws Exception {
        System.out.println("operationComplete 被执行到了 ");
    }
}
