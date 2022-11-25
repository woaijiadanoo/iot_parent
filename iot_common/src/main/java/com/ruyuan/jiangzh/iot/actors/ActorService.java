package com.ruyuan.jiangzh.iot.actors;

import com.ruyuan.jiangzh.iot.actors.msg.BaseMessage;
import com.ruyuan.jiangzh.iot.actors.msg.ToAllNodesMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.ToDeviceMsg;

public interface ActorService {

    void onMsg(BaseMessage msg);

    void onMsg(ToDeviceMsg msg);

    void broadcast(ToAllNodesMsg msg);

    void setRpcManager(RpcManager rpcManager);

    void onBroadcast(ToAllNodesMsg msg);
}
