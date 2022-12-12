package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrThingModelRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl.mapper.DeviceThingCaseMapper;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceThingCasePO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
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

    @Override
    public Optional<DeviceThingCasePO> findThingModelByDeviceId(DeviceId deviceId) {
        String deviceDataId = UUIDHelper.fromTimeUUID(deviceId.getUuid());

        QueryWrapper<DeviceThingCasePO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id", deviceDataId);

        List<DeviceThingCasePO> pos = thingCaseMapper.selectList(queryWrapper);
        if(pos != null && pos.size() > 0){
            return pos.stream().findFirst();
        }

        return Optional.empty();
    }

}
