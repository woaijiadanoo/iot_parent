package com.ruyuan.jiangzh.protol.infrastructure.protocol.impl;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.ruyuan.jiangzh.protol.infrastructure.acl.RpcManagerService;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.ProtocolApiService;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.ProtocolApiReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.ProtocolApiRespMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthRespMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jiangzheng
 * @version 1.0
 * @description: 协议统一处理机制
 */
@Service
public class ProtocolApiServiceImpl implements ProtocolApiService {

    @Autowired
    private RpcManagerService rpcManagerService;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public ListenableFuture<ProtocolApiRespMsg> handle(ProtocolApiReqMsg requestMsg) {
        // 要区分请求
        if(requestMsg.getDeviceAuthReqMsg() != null){
            // 处理鉴权的请求
            return validateAuth(requestMsg.getDeviceAuthReqMsg());
        }else{
            return getEmptyProtocolApiResponseFuture();
        }

    }

    // 处理鉴权的请求
    private ListenableFuture<ProtocolApiRespMsg> validateAuth(DeviceAuthReqMsg reqMsg){
        ListenableFuture<DeviceAuthRespMsg> deviceSecretFuture =
                rpcManagerService.findDeviceBySercet(reqMsg);

        if (deviceSecretFuture != null) {
            return getDeviceInfo(deviceSecretFuture);
        }else{
            return getEmptyProtocolApiResponseFuture();
        }
    }

    // 将DeviceAuthRespMsg 转换为 ProtocolApiRespMsg
    private ListenableFuture<ProtocolApiRespMsg> getDeviceInfo(ListenableFuture<DeviceAuthRespMsg> deviceSecretFuture){
        return Futures.transform(deviceSecretFuture, device -> {
            if(device != null && device.getDeviceId() != null){
                ProtocolApiRespMsg respMsg = new ProtocolApiRespMsg(device);
                return respMsg;
            }else{
                return getEmptyProtocolApiResponse();
            }
        }, executor);
    }


    // 返回空的ResponseFuture
    private ListenableFuture<ProtocolApiRespMsg> getEmptyProtocolApiResponseFuture(){
        return Futures.immediateFuture(getEmptyProtocolApiResponse());
    }

    // 返回空的ProtocolApiRespMsg
    private ProtocolApiRespMsg getEmptyProtocolApiResponse(){
        return new ProtocolApiRespMsg(new DeviceAuthRespMsg());
    }

}
