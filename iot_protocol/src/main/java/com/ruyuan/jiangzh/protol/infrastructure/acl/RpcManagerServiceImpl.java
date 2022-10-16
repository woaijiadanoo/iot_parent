package com.ruyuan.jiangzh.protol.infrastructure.acl;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthRespMsg;
import com.ruyuan.jiangzh.service.dto.DeviceSercetDTO;
import com.ruyuan.jiangzh.service.sdk.DeviceServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.Executors;

/**
 * @author jiangzheng
 * @version 1.0
 * @description: rpc调用的实现
 */
@Service
public class RpcManagerServiceImpl implements RpcManagerService{

    private ListeningExecutorService service =
            MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

    @Resource
    private DeviceServiceAPI deviceService;

    @Override
    public ListenableFuture<DeviceAuthRespMsg> findDeviceBySercet(DeviceAuthReqMsg requestMsg) {
        return service.submit(() -> {
            // 远程获取device相关的信息
            DeviceSercetDTO sercetDTO = deviceService.findDeviceBySercet(
                    requestMsg.getProductKey(),
                    requestMsg.getDeviceName(),
                    requestMsg.getDeviceSecret());

            // 封装成模块所需数据
            DeviceAuthRespMsg respMsg = DeviceAuthRespMsg.getRespByDeviceDTO(sercetDTO);

            return respMsg;
        });
    }





    @PreDestroy
    void onDestroy(){
        service.shutdown();
    }

}
