package com.ruyuan.jiangzh.iot.actors;

import com.ruyuan.jiangzh.iot.actors.msg.ToAllNodesMsg;

public interface RpcManager {

    void broadcast(ToAllNodesMsg msg);

}
