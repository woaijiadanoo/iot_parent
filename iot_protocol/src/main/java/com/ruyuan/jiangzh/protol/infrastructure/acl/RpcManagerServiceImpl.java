package com.ruyuan.jiangzh.protol.infrastructure.acl;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.ruyuan.jiangzh.iot.actors.ActorService;
import com.ruyuan.jiangzh.iot.actors.msg.ServerAddress;
import com.ruyuan.jiangzh.iot.actors.msg.ToAllNodesMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.FromDeviceMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.ToDeviceMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.FromDeviceOnlineMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.ToDeviceSessionEventMsg;
import com.ruyuan.jiangzh.iot.actors.msg.rule.TransportToRuleEngineActorMsgWrapper;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthRespMsg;
import com.ruyuan.jiangzh.service.dto.DeviceSercetDTO;
import com.ruyuan.jiangzh.service.sdk.DeviceServiceAPI;
import org.apache.dubbo.common.config.ReferenceCache;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.utils.SimpleReferenceCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

/**
 * @author jiangzheng
 * @version 1.0
 * @description: rpc调用的实现
 */
@Service
public class RpcManagerServiceImpl implements RpcManagerService{

    private final ReferenceCache referenceCache = SimpleReferenceCache.getCache();

    private static final String DEVICE_GROUP_NAME = "device";
    private static final String RULE_ENGINE_GROUP_NAME = "rule";

    private ListeningExecutorService service =
            MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

    @Resource
    private DeviceServiceAPI deviceService;

    @Override
    public ListenableFuture<DeviceAuthRespMsg> findDeviceBySercet(DeviceAuthReqMsg requestMsg) {
        return service.submit(() -> {
            // 远程获取device相关的信息
            DeviceSercetDTO sercetDTO = deviceService.findDeviceBySercet(
                    requestMsg.getProductKey(),
                    requestMsg.getDeviceName(),
                    requestMsg.getDeviceSecret());

            // 封装成模块所需数据
            DeviceAuthRespMsg respMsg = DeviceAuthRespMsg.getRespByDeviceDTO(sercetDTO);

            return respMsg;
        });
    }



    @Override
    public void broadcast(ToAllNodesMsg msg) {
        String hostAddr = null;
        // 可以通过dubbo的ProtocolConfig来获取
        int dubboPort = 20885;

        try {
            hostAddr = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        ServerAddress serverAddress = new ServerAddress(hostAddr, dubboPort);

        if(msg instanceof ToDeviceSessionEventMsg){
            ToDeviceSessionEventMsg sessionEventMsg = (ToDeviceSessionEventMsg) msg;
            sessionEventMsg.setServerAddress(serverAddress);

            ActorService actorService = getActorService(DEVICE_GROUP_NAME, true);
            actorService.onBroadcast(sessionEventMsg);
        }

    }

    @Override
    public void onMsg(FromDeviceMsg msg) {
        String hostAddr = null;
        // 可以通过dubbo的ProtocolConfig来获取
        int dubboPort = 20885;

        try {
            hostAddr = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        ServerAddress serverAddress = new ServerAddress(hostAddr, dubboPort);

        if(msg instanceof FromDeviceOnlineMsg){
            FromDeviceOnlineMsg onlineMsg = (FromDeviceOnlineMsg) msg;
            onlineMsg.setServerAddress(serverAddress);

            onDeviceOnlineMsg(onlineMsg);
        } else if (msg instanceof TransportToRuleEngineActorMsgWrapper){
            TransportToRuleEngineActorMsgWrapper msgWrapper = (TransportToRuleEngineActorMsgWrapper) msg;
            msgWrapper.setServerAddress(serverAddress);

            // 远程服务调用
            onTransportToRuleEngineActorMsgWrapper(msgWrapper);
        }


    }

    @Override
    public void onMsg(ToDeviceMsg msg) {

    }

    private void onTransportToRuleEngineActorMsgWrapper(TransportToRuleEngineActorMsgWrapper msgWrapper) {
        service.submit(() -> {
            ActorService actorService = getActorService(RULE_ENGINE_GROUP_NAME, false);
            actorService.onMsg(msgWrapper);
        });
    }

    private void onDeviceOnlineMsg(FromDeviceOnlineMsg onlineMsg) {
        service.submit(() -> {
            ActorService actorService = getActorService(DEVICE_GROUP_NAME, false);
            actorService.onMsg(onlineMsg);
        });
    }


    public ActorService getActorService(String groupName, boolean boardcast){
        ReferenceConfig<ActorService> referConfig = new ReferenceConfig<>();
        referConfig.setInterface(ActorService.class);
        referConfig.setGroup(groupName);
        if(boardcast){
            referConfig.setCluster("broadcast");
        }

        ActorService actorService = referenceCache.get(referConfig);
        return actorService;
    }

    @PreDestroy
    void onDestroy(){
        service.shutdown();
    }

}
