package com.ruyuan.jiangzh.protol.infrastructure.acl;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.ruyuan.jiangzh.iot.actors.msg.ToAllNodesMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.FromDeviceMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.ToDeviceMsg;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthReqMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth.DeviceAuthRespMsg;
import com.ruyuan.jiangzh.service.dto.DeviceSercetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@Service
public class TestRpcManagerServiceImpl implements RpcManagerService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ListeningExecutorService service =
            MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(30));

    @Override
    public void broadcast(ToAllNodesMsg msg) {


    }

    @Override
    public void onMsg(FromDeviceMsg msg) {

    }

    @Override
    public void onMsg(ToDeviceMsg msg) {

    }

    @Override
    public ListenableFuture<DeviceAuthRespMsg> findDeviceBySercet(DeviceAuthReqMsg requestMsg) {
        return service.submit(() -> {
            // 远程获取device相关的信息
            DeviceSercetDTO dto = new DeviceSercetDTO();
            dto.setTenantId(UUIDHelper.genUuid().toString());
            dto.setUserId(UUIDHelper.genUuid().toString());
            dto.setProductId(UUIDHelper.genUuid().toString());
            dto.setDeviceId(UUIDHelper.genUuid().toString());

            dto.setProductKey(requestMsg.getProductKey());
            dto.setDeviceSercet(requestMsg.getDeviceSecret());
            dto.setDeviceName(requestMsg.getDeviceName());

            dto.setProductSecret(requestMsg.getDeviceSecret());
            dto.setDeviceStatus(0);
            dto.setAutoActive(true);

            try {
                // 假设在设备模块进行三元组查询需要50毫秒左右的延迟
                Thread.sleep(50);
            }catch (Exception e){
                e.printStackTrace();
            }

            // 封装成模块所需数据
            DeviceAuthRespMsg respMsg = DeviceAuthRespMsg.getRespByDeviceDTO(dto);

            return respMsg;
        });
    }
}
