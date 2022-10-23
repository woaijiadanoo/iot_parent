package com.ruyuan.jiangzh.protol.infrastructure.acl;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.ruyuan.jiangzh.iot.actors.ActorService;
import com.ruyuan.jiangzh.iot.actors.msg.ServerAddress;
import com.ruyuan.jiangzh.iot.actors.msg.ToAllNodesMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.FromDeviceMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.FromDeviceOnlineMsg;
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

        System.out.println("RpcManagerServiceImpl serverAddress : " + serverAddress);

        if(msg instanceof FromDeviceOnlineMsg){
            FromDeviceOnlineMsg onlineMsg = (FromDeviceOnlineMsg) msg;
            onlineMsg.setServerAddress(serverAddress);

            onDeviceOnlineMsg(onlineMsg);
        }


    }

    private void onDeviceOnlineMsg(FromDeviceOnlineMsg onlineMsg) {
        service.submit(() -> {
            ActorService actorService = getActorService(DEVICE_GROUP_NAME);
            actorService.onMsg(onlineMsg);
        });
    }


    public ActorService getActorService(String groupName){
        ReferenceConfig<ActorService> referConfig = new ReferenceConfig<>();
        referConfig.setInterface(ActorService.class);
        referConfig.setGroup(groupName);

        ActorService actorService = referenceCache.get(referConfig);
        return actorService;
    }

    @PreDestroy
    void onDestroy(){
        service.shutdown();
    }

}
