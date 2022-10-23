package com.ruyuan.jiangzh.iot.actors.msg.device;

import com.ruyuan.jiangzh.iot.actors.msg.BaseMessage;
import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.actors.msg.MsgType;

public interface FromDeviceMsg extends IoTActorMessage {

    MsgType getMsgType();

}
