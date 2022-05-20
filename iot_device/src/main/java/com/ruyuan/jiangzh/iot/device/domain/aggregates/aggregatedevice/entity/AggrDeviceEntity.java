package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity;
import java.time.LocalDateTime;

import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.uuid.CreateTimeIdBase;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.common.DateUtils;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.common.JWTUtils;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceSercetRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DevicePO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceSercetInfoPO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.vo.DeviceInfosVO;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceStatusEnums;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceTypeEnums;
import com.ruyuan.jiangzh.iot.device.domain.vo.DeviceId;
import com.ruyuan.jiangzh.iot.device.domain.vo.ProductId;
import org.checkerframework.checker.units.qual.A;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
    这个是聚合根
 */
public class AggrDeviceEntity extends CreateTimeIdBase<DeviceId> implements Serializable {

    // tenant信息
    private UUID tenantId;
    // 用户信息
    private UUID userId;
    // 产品信息
    private ProductId productId;

    private String productName;


    private DeviceTypeEnums deviceType;

    private String region;
    private String deviceName;

    // 认证方式: secretKey
    private String authType;
    private String cnName;

    // IP地址
    private String ipAddr;
    // 固件版本
    private String fwVersion;
    // 激活时间
    private Long activeTime;
    // 最后在线时间
    private Long lastOnlineTime;
    // 设备状态
    private DeviceStatusEnums deviceStatus;

    private String sdkType;
    private String sdkVersion;

    // 三元组聚合实体
    private AggrDeviceSercetEntity deviceSercetEntity;

    private AggrDeviceSercetRepository deviceSercetRepository;


    private AggrDeviceRepository deviceRepository;

    public AggrDeviceEntity(){}
    public AggrDeviceEntity(AggrDeviceRepository deviceRepository, AggrDeviceSercetRepository deviceSercetRepository){
        this.deviceRepository = deviceRepository;
        this.deviceSercetRepository = deviceSercetRepository;
    }
    public AggrDeviceEntity(DeviceId deviceId,AggrDeviceRepository deviceRepository, AggrDeviceSercetRepository deviceSercetRepository){
        super(deviceId);
        this.deviceRepository = deviceRepository;
        this.deviceSercetRepository = deviceSercetRepository;
    }

    /*
        =======================service start===========================>
     */
    public AggrDeviceEntity saveDeviceEntity(){
        boolean isNew = this.getId() == null ? true : false;
        if(isNew){
            // 新增
            // 补充属性
            this.setRegion("shanghai");
            this.setAuthType("secretKey");
            this.setDeviceStatus(DeviceStatusEnums.NOT_ACTIVE);

            AggrDeviceSercetEntity deviceSercet = this.getDeviceSercetEntity();
            deviceSercet.setDeviceSecret(IoTStringUtils.getRandomString(6));

            this.setId(new DeviceId(UUIDHelper.genUuid()));
            DevicePO devicePO = this.entityToPO();
            DeviceSercetInfoPO deviceSercetInfoPO = deviceSercetEntity.entityToPO(this.getId());

            // 组织三元组等相关信息


            deviceRepository.insertDevice(devicePO);
            deviceSercetRepository.insertEntity(deviceSercetInfoPO);

            return this;
        }else{
            // 修改
            return null;
        }
    }

    /*
        列表查询，涵盖了根据产品获取设备列表【产品 -> 管理设备】
     */
    public PageDTO<AggrDeviceEntity> findDevices(PageDTO<AggrDeviceEntity> entityPage){
        // 拼接repository层的条件参数
        PageDTO<DevicePO> poPage = new PageDTO<>(entityPage.getNowPage(), entityPage.getPageSize());
        poPage.setConditions(entityPage.getConditions());

        poPage = deviceRepository.findDevices(poPage);

        List<AggrDeviceEntity> entities = poPage.getRecords().stream().map(
                po -> {
                    // 完成PO向entity的转化
                    AggrDeviceEntity entity = new AggrDeviceEntity();
                    entity.poToEntity(po);
                    return entity;
                }
        ).collect(Collectors.toList());

        entityPage.setResult(poPage.getTotals(),poPage.getTotalPages(), entities);

        return entityPage;
    }

    /*
        获取设备的相关数量【设备总数，激活设备，当前在线】
     */
    public DeviceInfosVO findDeviceInfos(ProductId productId){
        DeviceInfosVO deviceInfos = deviceRepository.findDeviceInfos(productId);
        return deviceInfos;
    }

    /*
        获取设备详情
     */
    public AggrDeviceEntity findDeviceById(DeviceId deviceId){
        // 获取对应的数据实体
        DevicePO devicePO = deviceRepository.findDeviceById(deviceId);
        DeviceSercetInfoPO deviceSercetPO = deviceSercetRepository.findDeviceSercetById(deviceId);

        // 数据实体转换为聚合
        AggrDeviceEntity aggrDeviceEntity = new AggrDeviceEntity();
        AggrDeviceSercetEntity aggrDeviceSercetEntity = new AggrDeviceSercetEntity();

        aggrDeviceEntity.poToEntity(devicePO);
        aggrDeviceSercetEntity.poToEntity(deviceSercetPO);

        // 拼装聚合根
        aggrDeviceEntity.setDeviceSercetEntity(aggrDeviceSercetEntity);

        return aggrDeviceEntity;
    }

    /*
        删除设备
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean delDeviceEntity(DeviceId deviceId){
        boolean delDeviceSercet = deviceSercetRepository.delDeviceSercetById(deviceId);
        boolean delDevice = deviceRepository.delDeviceById(deviceId);

        if(delDeviceSercet && delDevice){
            return true;
        }else{
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean delDeviceEntity(){
        boolean delDeviceSercet = deviceSercetRepository.delDeviceSercetById(this.getId());
        boolean delDevice = deviceRepository.delDeviceById(this.getId());

        if(delDeviceSercet && delDevice){
            return true;
        }else{
            return false;
        }
    }

    /*
        修改设备状态
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDeviceStatus(DeviceId deviceId, DeviceStatusEnums deviceStatusEnums){
        boolean updateDeviceSercetStatus = deviceSercetRepository.updateDeviceStatus(deviceId, deviceStatusEnums);
        boolean updateDeviceStatus = deviceRepository.updateDeviceStatus(deviceId, deviceStatusEnums);

        if(updateDeviceSercetStatus && updateDeviceStatus){
            return true;
        }else{
            return false;
        }
    }

    /*
        修改自动激活状态
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAutoActive(DeviceId deviceId, boolean autoActive){
        boolean updateDeviceSercetStatus = deviceSercetRepository.updateAutoActive(deviceId, autoActive);

        if(updateDeviceSercetStatus){
            return true;
        }else{
            return false;
        }
    }



    public void poToEntity(DevicePO po){
        this.setId(new DeviceId(UUIDHelper.fromStringId(po.getUuid())));
        this.setTenantId(UUIDHelper.fromStringId(po.getTenantId()));
        this.setUserId(UUIDHelper.fromStringId(po.getUserId()));
        this.setProductId(new ProductId(UUIDHelper.fromStringId(po.getProductId())));
        this.setProductName(po.getProductName());
        this.setDeviceType(DeviceTypeEnums.getByCode(po.getDeviceType()));
        this.setRegion(po.getReginName());
        this.setDeviceName(po.getDeviceName());
        this.setAuthType(po.getAuthType());
        this.setCnName(po.getCnName());
        this.setIpAddr(po.getIpAddr());
        this.setFwVersion(po.getFwVersion());
        this.setDeviceStatus(DeviceStatusEnums.getByCode(po.getDeviceStatus()));
        this.setSdkType(po.getSdkType());
        this.setSdkVersion(po.getSdkVersion());
        // po转entity的时候，别忘了转三元组对象
//        this.setDeviceSercetEntity();

        this.setActiveTime(DateUtils.getTimestampByLocalDateTime(po.getActiveTime()));
        this.setLastOnlineTime(DateUtils.getTimestampByLocalDateTime(po.getLastOnlineTime()));
    }

    public DevicePO entityToPO(){
        DevicePO devicePO = new DevicePO();
        devicePO.setUuid(UUIDHelper.fromTimeUUID(this.getId().getUuid()));
        devicePO.setUserId(UUIDHelper.fromTimeUUID(this.getUserId()));
        devicePO.setTenantId(UUIDHelper.fromTimeUUID(this.getTenantId()));
        devicePO.setProductId(UUIDHelper.fromTimeUUID(this.getProductId().getUuid()));
        devicePO.setProductName(this.getProductName());
        devicePO.setDeviceType(this.getDeviceType().getCode());
        devicePO.setReginName(this.getRegion());
        devicePO.setDeviceName(this.getDeviceName());
        devicePO.setCnName(this.getCnName());
        devicePO.setAuthType(this.getAuthType());
        devicePO.setIpAddr(this.getIpAddr());
        devicePO.setFwVersion(this.getFwVersion());
        if(this.getActiveTime() != null && this.getActiveTime() != 0L){
            devicePO.setActiveTime(DateUtils.getLocalDateTimebyTimestamp(this.getActiveTime()));
        }
        if(this.getLastOnlineTime() != null && this.getLastOnlineTime() != 0L){
            devicePO.setLastOnlineTime(DateUtils.getLocalDateTimebyTimestamp(this.getLastOnlineTime()));
        }

        devicePO.setDeviceStatus(this.getDeviceStatus().getCode());
        devicePO.setSdkType(this.getSdkType());
        devicePO.setSdkVersion(this.getSdkVersion());

        return devicePO;
    }

    /*
        =======================service end===========================>
     */

    @Override
    public void setId(DeviceId id) {
        super.setId(id);
    }

    public AggrDeviceSercetRepository getDeviceSercetRepository() {
        return deviceSercetRepository;
    }

    public void setDeviceSercetRepository(AggrDeviceSercetRepository deviceSercetRepository) {
        this.deviceSercetRepository = deviceSercetRepository;
    }

    public AggrDeviceRepository getDeviceRepository() {
        return deviceRepository;
    }

    public void setDeviceRepository(AggrDeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public AggrDeviceEntity(DeviceId deviceId){super(deviceId);}

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public ProductId getProductId() {
        return productId;
    }

    public void setProductId(ProductId productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public DeviceTypeEnums getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceTypeEnums deviceType) {
        this.deviceType = deviceType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getFwVersion() {
        return fwVersion;
    }

    public void setFwVersion(String fwVersion) {
        this.fwVersion = fwVersion;
    }

    public Long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Long activeTime) {
        this.activeTime = activeTime;
    }

    public Long getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Long lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public DeviceStatusEnums getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(DeviceStatusEnums deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getSdkType() {
        return sdkType;
    }

    public void setSdkType(String sdkType) {
        this.sdkType = sdkType;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public AggrDeviceSercetEntity getDeviceSercetEntity() {
        return deviceSercetEntity;
    }

    public void setDeviceSercetEntity(AggrDeviceSercetEntity deviceSercetEntity) {
        this.deviceSercetEntity = deviceSercetEntity;
    }

    @Override
    public String toString() {
        return "AggrDeviceEntity{" +
                "tenantId=" + tenantId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", deviceType=" + deviceType +
                ", region='" + region + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", authType='" + authType + '\'' +
                ", cnName='" + cnName + '\'' +
                ", ipAddr='" + ipAddr + '\'' +
                ", fwVersion='" + fwVersion + '\'' +
                ", activeTime=" + activeTime +
                ", lastOnlineTime=" + lastOnlineTime +
                ", deviceStatus=" + deviceStatus +
                ", sdkType='" + sdkType + '\'' +
                ", sdkVersion='" + sdkVersion + '\'' +
                ", deviceSercetEntity=" + deviceSercetEntity +
                '}';
    }
}
