package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DevicePO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.vo.DeviceInfosVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface DeviceMapper extends BaseMapper<DevicePO> {

    DeviceInfosVO findDeviceInfos(@Param("productId") String productId);

}
