package com.ruyuan.jiangzh.iot.rule.infrastructure.actors.rpc;

import com.ruyuan.jiangzh.iot.actors.ActorService;
import com.ruyuan.jiangzh.iot.actors.RpcManager;
import com.ruyuan.jiangzh.iot.actors.msg.ToAllNodesMsg;
import org.apache.dubbo.common.config.ReferenceCache;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.utils.SimpleReferenceCache;

public class RuleEngineRpcManager implements RpcManager {

    private final ReferenceCache referenceCache = SimpleReferenceCache.getCache();

    private static final String GROUP_NAME = "rule";

    @Override
    public void broadcast(ToAllNodesMsg msg) {
        ActorService actorService = getActorService(GROUP_NAME);
        // 去进行一次广播
        actorService.onBroadcast(msg);
    }

    /*
        千万注意， 这里是ActorService的Consumer
     */
    public ActorService getActorService(String groupName){
        ReferenceConfig<ActorService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(ActorService.class);
        referenceConfig.setGroup(groupName);
        referenceConfig.setCluster("broadcast");

        ActorService actorService = referenceCache.get(referenceConfig);

        return actorService;
    }

}
