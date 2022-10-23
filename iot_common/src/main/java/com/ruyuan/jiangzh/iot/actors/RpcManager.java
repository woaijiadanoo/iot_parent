package com.ruyuan.jiangzh.iot.actors;

import com.ruyuan.jiangzh.iot.actors.msg.ToAllNodesMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.FromDeviceMsg;

public interface RpcManager {

    void broadcast(ToAllNodesMsg msg);

    void onMsg(FromDeviceMsg msg);

}
