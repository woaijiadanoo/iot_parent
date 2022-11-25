package com.ruyuan.jiangzh.iot.actors;

import com.ruyuan.jiangzh.iot.actors.msg.ToAllNodesMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.FromDeviceMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.ToDeviceMsg;

public interface RpcManager {

    void broadcast(ToAllNodesMsg msg);

    void onMsg(FromDeviceMsg msg);

    void onMsg(ToDeviceMsg msg);


}
