package com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.common.ProtocolServiceCallback;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.ProtocolApiReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.ProtocolApiRespMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthRespMsg;
import com.ruyuan.jiangzh.service.sdk.DeviceServiceAPI;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import jnr.ffi.Struct;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

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
                processConnect(ctx, (MqttConnectMessage) msg);
                break;
            case DISCONNECT:

            default:
                break;
        }

    }

    // 处理连接请求
    private void processConnect(ChannelHandlerContext ctx, MqttConnectMessage msg) {
        /*
            鉴权，是否有权限进行连接
            1、获取三元组信息【productKey，DeviceName，DeviceSerct】
            2、通过三元组信息来获取设备详情
         */
        // 1、获取三元组信息【productKey，DeviceName，DeviceSerct】
        String productKey = msg.payload().userName();
        String deviceName = "";
        try {
            deviceName = new String(msg.payload().passwordInBytes(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            ctx.writeAndFlush(createMqttConnAckMsg(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_AUTHENTICATION_METHOD));
        }
        String deviceSerct = msg.payload().clientIdentifier();

        DeviceAuthReqMsg deviceAuthReqMsg = new DeviceAuthReqMsg(productKey,deviceName,deviceSerct);

        // 2、通过三元组信息来获取设备详情
        context.getProtocolService().process(deviceAuthReqMsg, new ProtocolServiceCallback<DeviceAuthRespMsg>() {
            @Override
            public void onSuccess(DeviceAuthRespMsg msg) {
                onValidateDeviceResponse(msg, ctx);
            }

            @Override
            public void onError(Throwable e) {
                ctx.writeAndFlush(createMqttConnAckMsg(MqttConnectReturnCode.CONNECTION_REFUSED_SERVER_UNAVAILABLE));
                ctx.close();
            }
        });
    }


    private void onValidateDeviceResponse(DeviceAuthRespMsg msg, ChannelHandlerContext ctx) {
        // 验证一下返回的device是否有效
        if(msg.getDeviceId() == null){
            ctx.writeAndFlush(createMqttConnAckMsg(MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED));
            ctx.close();
        }else{
            ctx.writeAndFlush(createMqttConnAckMsg(MqttConnectReturnCode.CONNECTION_ACCEPTED));
        }
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