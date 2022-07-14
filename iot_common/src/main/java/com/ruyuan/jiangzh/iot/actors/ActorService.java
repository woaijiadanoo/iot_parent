package com.ruyuan.jiangzh.iot.actors;

import com.ruyuan.jiangzh.iot.actors.msg.BaseMessage;

public interface ActorService {

    void onMsg(BaseMessage msg);

}
