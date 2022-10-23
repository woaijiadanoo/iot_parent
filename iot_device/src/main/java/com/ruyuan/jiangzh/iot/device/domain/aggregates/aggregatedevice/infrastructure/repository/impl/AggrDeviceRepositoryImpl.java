package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.common.DateUtils;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl.mapper.DeviceMapper;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DevicePO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.vo.DeviceInfosVO;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceStatusEnums;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.device.ProductId;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
public class AggrDeviceRepositoryImpl implements AggrDeviceRepository {

    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public DevicePO insertDevice(DevicePO devicePO) {
        // 数据验证

        // 数据填充

        deviceMapper.insert(devicePO);

        return devicePO;
    }

    @Override
    public PageDTO<DevicePO> findDevices(PageDTO<DevicePO> pageDTO) {
        // 组织mybatis-plus的两个核心对象 ipage , queryWrapper
        IPage<DevicePO> iPage = new Page<>(pageDTO.getNowPage(),pageDTO.getPageSize());
        QueryWrapper<DevicePO> queryWrapper = null;
        if(pageDTO.getConditions().size() > 0){
            queryWrapper = new QueryWrapper<>();
            Set<String> keys = pageDTO.getConditions().keySet();
            for(String key : keys){
                // 拼接查询条件
                spellCondition(queryWrapper, key, pageDTO.getConditions().get(key));
            }
        }

        IPage<DevicePO> page = deviceMapper.selectPage(iPage, queryWrapper);

        // 封装返回对象
        pageDTO.setResult(page.getTotal(), page.getPages(), page.getRecords());

        return pageDTO;
    }

    @Override
    public DeviceInfosVO findDeviceInfos(ProductId productId) {
        String productIdStr = null;
        if(productId != null){
            productIdStr = UUIDHelper.fromTimeUUID(productId.getUuid());
        }

        // 在线设备其实是从redis中进行获取的

        DeviceInfosVO deviceInfos = deviceMapper.findDeviceInfos(productIdStr);
        return deviceInfos;
    }

    @Override
    public DevicePO findDeviceById(DeviceId deviceId) {
        String dataId = UUIDHelper.fromTimeUUID(deviceId.getUuid());
        DevicePO devicePO = deviceMapper.selectById(dataId);
        return devicePO;
    }

    @Override
    public boolean delDeviceById(DeviceId deviceId) {
        String dataId = UUIDHelper.fromTimeUUID(deviceId.getUuid());
        deviceMapper.deleteById(dataId);
        return true;
    }

    @Override
    public boolean updateDeviceStatus(DeviceId deviceId, DeviceStatusEnums deviceStatusEnums) {
        DevicePO devicePO = new DevicePO();
        devicePO.setUuid(UUIDHelper.fromTimeUUID(deviceId.getUuid()));
        devicePO.setDeviceStatus(deviceStatusEnums.getCode());

        deviceMapper.updateById(devicePO);

        return true;
    }

    @Override
    public boolean updateDeviceStatusAndOnlineTime(DeviceId deviceId, DeviceStatusEnums deviceStatusEnums, Long onlineTimestamp) {
        DevicePO devicePO = new DevicePO();
        devicePO.setUuid(UUIDHelper.fromTimeUUID(deviceId.getUuid()));
        devicePO.setDeviceStatus(deviceStatusEnums.getCode());
        if(onlineTimestamp != null){
            devicePO.setLastOnlineTime(DateUtils.getDateByTimestamp(onlineTimestamp));
        }

        deviceMapper.updateById(devicePO);

        return true;
    }

    private void spellCondition(QueryWrapper queryWrapper, String fieldKey, Object fieldValue){
        if(IoTStringUtils.isBlank(fieldKey)){
            if("productId".equalsIgnoreCase(fieldKey)){
                // 这里传入的UUID版本的productId
                queryWrapper.eq("product_id", UUIDHelper.fromTimeUUID(IoTStringUtils.toUUID(fieldKey)));
            }else if("deviceName".equalsIgnoreCase(fieldKey)){
                queryWrapper.like("device_name", fieldValue);
            }else if("cnName".equalsIgnoreCase(fieldKey)){
                queryWrapper.like("cn_name", fieldValue);
            }
        }
    }

}
