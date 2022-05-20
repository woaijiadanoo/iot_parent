package com.ruyuan.jiangzh.iot.device.interfaces.controller;

import com.google.common.collect.Maps;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.common.AuthorityRole;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceSercetEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.vo.DeviceInfosVO;
import com.ruyuan.jiangzh.iot.device.domain.domainservice.DeviceDomainService;
import com.ruyuan.jiangzh.iot.device.domain.vo.DeviceId;
import com.ruyuan.jiangzh.iot.device.domain.vo.ProductId;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceTypeEnums;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceStatusEnums;

import com.ruyuan.jiangzh.iot.base.web.BaseController;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.base.security.IoTSecurityUser;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.factory.AggrDeviceFactory;
import com.ruyuan.jiangzh.iot.device.interfaces.dto.DeviceDTO;
import com.ruyuan.jiangzh.iot.device.interfaces.dto.DeviceDetailDTO;
import com.ruyuan.jiangzh.iot.device.interfaces.dto.DeviceStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1")
public class DeviceController extends BaseController {

    @Autowired
    private AggrDeviceFactory deviceFactory;

    @Autowired
    private DeviceDomainService deviceDomainService;

    private static final String DEVICE_ID_IS_NULL = "device.device_id_is_null";

    /*
        http://localhost:8082/api/v1/device?ruyuan_name=ruyuan_00

        {
            "productId":"9a97f910-d28f-11ec-a05f-dbd50d7d93eb",
            "deviceName":"ry_device_01",
            "cnName":"儒猿设备第一台"
        }
     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN', 'USER')")
    @RequestMapping(value = "device", method = RequestMethod.POST)
    public RespDTO saveDevice(@RequestBody DeviceDTO deviceDTO){
        // 验证deviceDTO数据的准确性

        IoTSecurityUser currentUser = getCurrentUser();

        // 所有跟接口层有关的输入参数，在这里进行赋值操作
        AggrDeviceEntity deviceEntity = deviceFactory.getDevice();
        DeviceDTO.dtoToEntity(deviceEntity, deviceDTO);

        // 直接进行保存操作【domainService的一个最主要的用法，就是同一个限界上下文的不同实体的操作】
        deviceDomainService.saveDeviceEntity(deviceEntity);

        deviceDTO.setDeviceId(deviceEntity.getId().toString());

        return RespDTO.success(deviceDTO);
    }

    /*
        http://localhost:8082/api/v1/devices?ruyuan_name=ruyuan_00
     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN', 'USER')")
    @RequestMapping(value = "devices", method = RequestMethod.GET)
    public RespDTO devices(
            @RequestParam(name = "nowPage", required = false, defaultValue = "1") long nowPage,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") long pageSize,
            @RequestParam(name = "productId", required = false) String productId,
            @RequestParam(name = "deviceName", required = false) String deviceName,
            @RequestParam(name = "cnName", required = false) String cnName){
        IoTSecurityUser currentUser = getCurrentUser();
        ProductId pId = null;
        // 对于参数的验证，比如nowPage不能小于1，pageSize不能小于1
        PageDTO<AggrDeviceEntity> pageDTO = new PageDTO<>(nowPage, pageSize);
        if(IoTStringUtils.isNotBlank(productId)){
            pageDTO.spellCondition("productId", productId);
            pId = new ProductId(toUUID(productId));
        }
        if(IoTStringUtils.isNotBlank(deviceName)){
            pageDTO.spellCondition("deviceName", productId);
        }
        if(IoTStringUtils.isNotBlank(cnName)){
            pageDTO.spellCondition("cnName", productId);
        }

        // 获取设备列表【带条件带翻页】
        AggrDeviceEntity aggrDeviceEntity = deviceFactory.getDevice();
        PageDTO devices = aggrDeviceEntity.findDevices(pageDTO);

        List<AggrDeviceEntity> deviceEntities = devices.getRecords();
        List<DeviceDTO> deviceDTOs =
                deviceEntities.stream().map(entity -> DeviceDTO.entityToDTO(entity)).collect(Collectors.toList());

        devices.setRecords(deviceDTOs);

        // 获取设备总数量信息【根据productId进行筛选 -> 如果productid为空，则全部查询】
        DeviceInfosVO deviceInfo = aggrDeviceEntity.findDeviceInfos(pId);

        // 拼装返回参数
        Map<String, Object> result = Maps.newHashMap();
        result.put("devices", devices);
        result.put("deviceInfo", deviceInfo);

        return RespDTO.success(result);
    }

    /*
        获取设备详情
     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN', 'USER')")
    @RequestMapping(value = "/product/{productId}/device/{deviceId}", method = RequestMethod.GET)
    public RespDTO findDeviceById(
            @PathVariable("productId") String productIdStr,
            @PathVariable("deviceId") String deviceIdStr){
        checkParameter(deviceIdStr, DEVICE_ID_IS_NULL);
        // 正常实现的时候， 应该再定义一个错误信息，在这里我们就不赘述了
        checkParameter(productIdStr, DEVICE_ID_IS_NULL);
        DeviceId deviceId = new DeviceId(toUUID(deviceIdStr));
        ProductId productId = new ProductId(toUUID(productIdStr));
        // 通过聚合根获取对应的聚合
        // 如果做好的再好一点，应该传入tenantId， userId， productId
        AggrDeviceEntity entity = deviceFactory.getDeviceById(deviceId);

        IoTSecurityUser currentUser = getCurrentUser();
        if(!entity.getProductId().equals(productId)){
            // 应该给一个权限不足， 或者没有数据的提示
        }
        if(currentUser.getAuthorityRole().equals(AuthorityRole.TENANT_ADMIN)){
            if(!entity.getTenantId().equals(currentUser.getTenantId())){
                // 应该给一个权限不足， 或者没有数据的提示
            }
        }else if(currentUser.getAuthorityRole().equals(AuthorityRole.USER)){
            // 应该给一个权限不足， 或者没有数据的提示
        }
        // 拼装返回结果
        DeviceDetailDTO detailDTO = DeviceDetailDTO.entityToDTO(entity);

        return RespDTO.success(checkNotNull(detailDTO));
    }

    /*
        获取设备详情
     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN', 'USER')")
    @RequestMapping(value = "/product/{productId}/device/{deviceId}", method = RequestMethod.DELETE)
    public RespDTO delDevice(
            @PathVariable("productId") String productIdStr,
            @PathVariable("deviceId") String deviceIdStr){
        checkParameter(deviceIdStr, DEVICE_ID_IS_NULL);
        // 正常实现的时候， 应该再定义一个错误信息，在这里我们就不赘述了
        checkParameter(productIdStr, DEVICE_ID_IS_NULL);
        DeviceId deviceId = new DeviceId(toUUID(deviceIdStr));
        ProductId productId = new ProductId(toUUID(productIdStr));
        // 通过聚合根获取对应的聚合
        AggrDeviceEntity deviceEntity = deviceFactory.getDeviceById(deviceId);

        IoTSecurityUser currentUser = getCurrentUser();
        if(!deviceEntity.getProductId().equals(productId)){
            // 应该给一个权限不足， 或者没有数据的提示[AppException]
        }
        if(currentUser.getAuthorityRole().equals(AuthorityRole.TENANT_ADMIN)){
            if(!deviceEntity.getTenantId().equals(currentUser.getTenantId())){
                // 应该给一个权限不足， 或者没有数据的提示[AppException]
            }
        }else if(currentUser.getAuthorityRole().equals(AuthorityRole.USER)){
            // 应该给一个权限不足， 或者没有数据的提示[AppException]
        }

        deviceEntity.delDeviceEntity();

        return RespDTO.success();
    }


    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN', 'USER')")
    @RequestMapping(value = "/product/{productId}/device/{deviceId}:updateDeviceStatus", method = RequestMethod.PUT)
    public RespDTO updateDeviceStatus(
            @PathVariable("productId") String productIdStr,
            @PathVariable("deviceId") String deviceIdStr,
            @RequestBody DeviceStatusDTO deviceStatusDTO
            ) {
        checkParameter(deviceIdStr, DEVICE_ID_IS_NULL);
        // 正常实现的时候， 应该再定义一个错误信息，在这里我们就不赘述了
        checkParameter(productIdStr, DEVICE_ID_IS_NULL);
        DeviceId deviceId = new DeviceId(toUUID(deviceIdStr));
        ProductId productId = new ProductId(toUUID(productIdStr));

        // 根据DeviceId获取Device
        AggrDeviceEntity deviceEntity = deviceFactory.getDeviceById(deviceId);
        // 然后根据用户的tenant和user编号+productId， 判断是否有权限处理这个设备
        deviceEntity.updateDeviceStatus(DeviceStatusEnums.getByCode(deviceStatusDTO.getDeviceStatus()));

        return RespDTO.success();
    }

}
