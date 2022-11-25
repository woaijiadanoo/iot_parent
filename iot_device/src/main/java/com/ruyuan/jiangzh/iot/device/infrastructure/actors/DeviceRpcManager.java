package com.ruyuan.jiangzh.iot.device.infrastructure.actors;

import com.ruyuan.jiangzh.iot.actors.ActorService;
import com.ruyuan.jiangzh.iot.actors.RpcManager;
import com.ruyuan.jiangzh.iot.actors.msg.ServerAddress;
import com.ruyuan.jiangzh.iot.actors.msg.ToAllNodesMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.FromDeviceMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.ToDeviceMsg;
import org.apache.dubbo.common.config.ReferenceCache;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.utils.SimpleReferenceCache;
import org.springframework.stereotype.Component;

@Component
public class DeviceRpcManager implements RpcManager {

    private final ReferenceCache referenceCache = SimpleReferenceCache.getCache();

    private static final String PROTOCOL_GROUP_NAME = "protocol";

    @Override
    public void broadcast(ToAllNodesMsg msg) {

    }

    @Override
    public void onMsg(FromDeviceMsg msg) {

    }

    @Override
    public void onMsg(ToDeviceMsg msg) {
        System.err.println("DeviceRpcManager msg : "+ msg);
        // 通知所有的网关节点，接收消息
        ActorService actorService = getActorService(PROTOCOL_GROUP_NAME, msg.getServerAddress(), msg.broadcast());
        // 进行远程RPC调用
        actorService.onMsg(msg);
    }

    public ActorService getActorService(String groupName, ServerAddress serverAddress, boolean broadcast){
        ReferenceConfig<ActorService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(ActorService.class);
        referenceConfig.setGroup(groupName);
        if(broadcast){
            referenceConfig.setCluster("broadcast");
        }

        ActorService actorService = referenceCache.get(referenceConfig);
        return actorService;
    }

}
