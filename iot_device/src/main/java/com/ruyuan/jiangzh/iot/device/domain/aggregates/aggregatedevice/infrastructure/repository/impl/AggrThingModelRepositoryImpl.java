package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl;

import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrThingModelRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl.mapper.DeviceThingCaseMapper;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceThingCasePO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

@Component
public class AggrThingModelRepositoryImpl implements AggrThingModelRepository {

    @Resource
    private DeviceThingCaseMapper thingCaseMapper;

    @Override
    public void saveThingModel(DeviceThingCasePO thingCase) {
        try {
            if(thingCase != null){
                if(thingCase.getUuid() != null){
                    // 修改逻辑
                    thingCaseMapper.updateById(thingCase);
                }else{
                    // 新增的逻辑
                    UUID thingModelId = UUIDHelper.genUuid();
                    thingCase.setUuid(UUIDHelper.fromTimeUUID(thingModelId));

                    thingCaseMapper.insert(thingCase);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
