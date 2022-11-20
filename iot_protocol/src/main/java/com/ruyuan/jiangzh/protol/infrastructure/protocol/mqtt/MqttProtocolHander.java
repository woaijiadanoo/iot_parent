package com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt;

import com.datastax.oss.driver.shaded.guava.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.ruyuan.jiangzh.iot.actors.msg.messages.FromDeviceOnlineMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.SubscribeToAttrUpdateMsg;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.AbstractProtocolService;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.ProtocolService;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.common.ProtocolServiceCallback;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.exceptions.AdaptorException;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthRespMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt.common.MqttProtocolAdaptor;
import com.ruyuan.jiangzh.iot.actors.msg.rule.PostTelemetryMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.vo.DeviceInfoVO;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.vo.SessionEventEnum;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.vo.SessionInfoVO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import static com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt.common.MqttTopics.*;
import static io.netty.handler.codec.mqtt.MqttMessageType.*;
import static io.netty.handler.codec.mqtt.MqttQoS.*;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class MqttProtocolHander extends ChannelInboundHandlerAdapter
        implements GenericFutureListener<Future<? super Void>>, SessionMsgListener {

    private final MqttProtocolContext context;

    private volatile InetSocketAddress address;

    private final UUID sessionId;

    private volatile DeviceSessionCtx deviceSessionCtx;

    private ProtocolService protocolService;

    private SessionInfoVO sessionInfo;

    private final MqttProtocolAdaptor adaptor;

    private static final String PRODUCT_KEY = "productKey";
    private static final String DEVICE_NAME = "deviceName";

    private static final Gson GSON = new Gson();
    private static final Charset UTF8 = Charset.forName("UTF-8");


    public MqttProtocolHander(MqttProtocolContext context){
        this.context = context;

        this.adaptor = context.getProtocolAdaptor();

        this.protocolService = context.getProtocolService();

        sessionId = UUIDHelper.genUuid();
        deviceSessionCtx = new DeviceSessionCtx(sessionId);

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
            case PUBLISH:
                processPublish(ctx,(MqttPublishMessage) msg);
                break;
            case SUBSCRIBE:
                processSubscribe(ctx,(MqttSubscribeMessage) msg);
                break;
            case DISCONNECT:
                processDisConnect(ctx);
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
        protocolService.process(deviceAuthReqMsg, new ProtocolServiceCallback<DeviceAuthRespMsg>() {
            @Override
            public void onSuccess(DeviceAuthRespMsg msg) {
                // 鉴权流程
                onValidateDeviceResponse(msg, ctx);

                // 修改设备的激活状态以及在线时间
                FromDeviceOnlineMsg onlineMsg = new FromDeviceOnlineMsg();
                onlineMsg.setDeviceId(msg.getDeviceId());
                onlineMsg.setTenantId(msg.getTenantId());
                onlineMsg.setUserId(msg.getUserId());
                onlineMsg.setProductId(msg.getProductId());
                onlineMsg.setOnlineTime(System.currentTimeMillis());

                context.getProtocolService().process(onlineMsg);

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
            // 链接无效的情况
            ctx.writeAndFlush(createMqttConnAckMsg(MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED));
            ctx.close();
        }else{
            // 链接有效的情况
            DeviceInfoVO deviceInfoVO = new DeviceInfoVO();
            deviceInfoVO.setDeviceId(msg.getDeviceId());
            deviceInfoVO.setTenantId(msg.getTenantId());
            deviceInfoVO.setProductId(msg.getProductId());

            deviceSessionCtx.setDeviceInfo(deviceInfoVO);
            deviceSessionCtx.setChannel(ctx);

            // session信息管理
            sessionInfo = new SessionInfoVO(sessionId, msg.getDeviceId(), msg.getTenantId());

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
        if(deviceSessionCtx.isConnected()){
            // 广播给所有的设备模块， 我被干掉了， 你也把actor关掉把
            protocolService.process(sessionInfo,
                    AbstractProtocolService.getSessionEventMsg(SessionEventEnum.CLOSE), null);
        }
    }

    @Override
    public void operationComplete(Future<? super Void> future) throws Exception {
        // 完全看项目需求
//        if(deviceSessionCtx.isConnected()){
//            // 广播给所有的设备模块， 我被干掉了， 你也把actor关掉把
//            protocolService.process(sessionInfo,
//                    AbstractProtocolService.getSessionEventMsg(SessionEventEnum.CLOSE), null);
//        }
    }



    /*
        处理所有的publish请求
     */
    private void processPublish(ChannelHandlerContext ctx, MqttPublishMessage mqttMsg) {
        // 检查连接情况
        if(!checkConnected(ctx, mqttMsg)){
            return;
        }

        // 获取关键元数据
        String topicName = mqttMsg.variableHeader().topicName();
        int msgId = mqttMsg.variableHeader().packetId();

        System.err.println("MqttProtocolHander topicName : "+topicName + " , msgId : " + msgId);

        // 区分业务类型
        // /sys 设备标签
        if(topicName.startsWith(BASE_DEVICE_TOPIC_PER)){
            if(topicName.endsWith(UPLOAD_DEVICE_TAG_TOPIC)){
                // 设备上报标签数据
                Map<String, String> topicMetaData = parseTopicName(topicName, 1, 2);
                onDeviceTagUpload(ctx, mqttMsg, msgId, topicMetaData);
            }
        }
    }

    private void onDeviceTagUpload(ChannelHandlerContext ctx, MqttPublishMessage mqttMsg, int msgId, Map<String, String> topicMetaData) {
        try {
            PostTelemetryMsg postTelemetryMsg = adaptor.convertToPostTelemetryMsg(deviceSessionCtx, mqttMsg);
            protocolService.process(sessionInfo, postTelemetryMsg, getPubAckCallback(ctx, msgId, postTelemetryMsg));
        } catch (AdaptorException e) {
            ctx.close();
        }
    }

    private <T> ProtocolServiceCallback getPubAckCallback(ChannelHandlerContext ctx, int msgId, T msg){
        return new ProtocolServiceCallback() {
            @Override
            public void onSuccess(Object msg) {
                if(msgId > 0){
                    ctx.writeAndFlush(createMqttPubAckMsg(msgId));
                }
            }

            @Override
            public void onError(Throwable e) {
                // 极端的处理方式就是直接关闭链接，节省服务器资源
                processDisConnect(ctx);
            }
        };
    }


    /*
        构建一个publish的返回消息
     */
    private MqttPubAckMessage createMqttPubAckMsg(int msgId) {
        MqttFixedHeader mqttFixedHeader =
                new MqttFixedHeader(PUBACK, false, AT_LEAST_ONCE, false, 0);

        MqttMessageIdVariableHeader mqttMessageIdVariableHeader =
                MqttMessageIdVariableHeader.from(msgId);

        return new MqttPubAckMessage(mqttFixedHeader, mqttMessageIdVariableHeader);
    }


    // 解析出productKey和deviceName
    private Map<String,String> parseTopicName(String topicName, int productKeyIndex,int deviceNameIndex){
        String[] topicPaths = topicName.split("/");
        Map<String,String> result = null;
        if(topicPaths.length >= productKeyIndex && topicPaths.length >= deviceNameIndex){
            result = Maps.newHashMap();
            result.put(PRODUCT_KEY,  topicPaths[productKeyIndex]);
            result.put(DEVICE_NAME, topicPaths[deviceNameIndex]);
        }
        return result;
    }

    private boolean checkConnected(ChannelHandlerContext ctx, MqttMessage mqttMsg) {
        if(deviceSessionCtx.isConnected()){
            return true;
        }else{
            ctx.close();
            return false;
        }
    }


    /*
        处理MQTT订阅相关的内容
     */
    private void processSubscribe(ChannelHandlerContext ctx, MqttSubscribeMessage mqttMsg) {
        // 检查连接是不是正常
        if(!checkConnected(ctx, mqttMsg)){
            return ;
        }

        int msgId = mqttMsg.variableHeader().messageId();
        // 订阅某个或某几个topic的信息
        List<Integer> grantedQosList = Lists.newArrayList();
        for(MqttTopicSubscription subscrption : mqttMsg.payload().topicSubscriptions()){
            String topicName = subscrption.topicName();
            MqttQoS reqQos = subscrption.qualityOfService();

            // 分类别 -> topicName
            /*
                OTA
                属性变更
                状态变更
             */
            // 区分业务类型
            // /sys 设备标签
            if(topicName.startsWith(BASE_DEVICE_TOPIC_PER)){
                if(topicName.endsWith(DEVICE_ATTR_UPDATE_SUB)){
                    // 设备上报标签数据
                    Map<String, String> topicMetaData = parseTopicName(topicName, 1, 2);
                    protocolService.process(sessionInfo, new SubscribeToAttrUpdateMsg(), null);
                }
            }
        }

        // 告诉设备端， 你的订阅已经成功了
        ctx.writeAndFlush(createSubAckMessage(msgId, grantedQosList));

        // TODO 测试，给设备推送订阅消息, 测试代码，记得删除
        try {
            Thread.sleep(5000L);

            for(int i=0; i<5; i++){
                String message = "{\"testKey\":\"ry \""+i+", \"testValue\":  \"hahaha\"}";
                Thread.sleep(1000L);
                // 相当于将服务端请求封装成publishMessage，然后推送给设备端
                onAttributeUpdate(message);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // 当订阅的Topics有消息，并且接收到信息

    // 创建一个publish的请求，并且发送给对应的Topics
    public void onAttributeUpdate(String message){
        Optional<MqttMessage> messageOptional = convertToPublish(deviceSessionCtx, message);
        // 通过通道传递消息给设备
        messageOptional.ifPresent(deviceSessionCtx.getChannel() :: writeAndFlush);
    }

    private Optional<MqttMessage> convertToPublish(DeviceSessionCtx deviceSessionCtx, String message) {
        String topicName = "/sys/i4g423najn/ry_device_02/thing/device/attr/update";
        MqttPublishMessage result = createMqttPublishMsg(deviceSessionCtx, topicName, message);
        if(result == null){
            return Optional.empty();
        }else{
            return Optional.of(result);
        }
    }

    private MqttPublishMessage createMqttPublishMsg(DeviceSessionCtx deviceSessionCtx, String topicName, String message) {
        MqttFixedHeader fixedHeader = new MqttFixedHeader(PUBLISH,false, AT_LEAST_ONCE, false, 0);
        MqttPublishVariableHeader variableHeader = new MqttPublishVariableHeader(topicName, deviceSessionCtx.nextMsgId());
        ByteBuf payload = ByteBufAllocator.DEFAULT.buffer();

        payload.writeBytes(GSON.toJson(message).getBytes(UTF8));

        return new MqttPublishMessage(fixedHeader, variableHeader, payload);
    }


    private MqttSubAckMessage createSubAckMessage(int msgId, List<Integer> grantedQosList) {
        // MQTT 固定头
        MqttFixedHeader mqttFixedHeader
                = new MqttFixedHeader(SUBACK, false, AT_LEAST_ONCE, false, 0);
        // MQTT可变头
        MqttMessageIdVariableHeader mqttMessageIdVariableHeader =
                MqttMessageIdVariableHeader.from(msgId);
        // payload
        MqttSubAckPayload mqttSubAckPayload = new MqttSubAckPayload(grantedQosList);

        return new MqttSubAckMessage(mqttFixedHeader, mqttMessageIdVariableHeader, mqttSubAckPayload);
    }

}
