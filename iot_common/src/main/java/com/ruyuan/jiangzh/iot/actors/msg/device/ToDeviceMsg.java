package com.ruyuan.jiangzh.iot.actors.msg.device;

import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.actors.msg.ServerAddress;

public interface ToDeviceMsg extends IoTActorMessage {

    boolean broadcast();

    String getSessionId();

    ServerAddress getServerAddress();

    String getMsg();

}
