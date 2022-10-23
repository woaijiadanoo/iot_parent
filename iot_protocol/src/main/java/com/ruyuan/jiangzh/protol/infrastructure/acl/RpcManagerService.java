package com.ruyuan.jiangzh.protol.infrastructure.acl;

import com.google.common.util.concurrent.ListenableFuture;
import com.ruyuan.jiangzh.iot.actors.RpcManager;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthRespMsg;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public interface RpcManagerService extends RpcManager {

    ListenableFuture<DeviceAuthRespMsg> findDeviceBySercet(DeviceAuthReqMsg requestMsg);


}
