package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.uuid.CreateTimeIdBase;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.UserId;
import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.base.web.RespCodeEnum;
import com.ruyuan.jiangzh.iot.common.DateUtils;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceSercetRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrThingModelRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DevicePO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceSercetInfoPO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceThingCasePO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.vo.DeviceInfosVO;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceStatusEnums;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceTypeEnums;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.device.ProductId;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/*
    这个是聚合根
 */
public class AggrDeviceEntity extends CreateTimeIdBase<DeviceId> implements Serializable {

    // tenant信息
    private TenantId tenantId;
    // 用户信息
    private UserId userId;
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

    private AggrThingEntity thingEntity;

    private AggrDeviceSercetRepository deviceSercetRepository;


    private AggrDeviceRepository deviceRepository;

    private AggrThingModelRepository thingModelRepository;

    private static final String JSON_FORMAT_ERROR = "thing.thing_format_error";

    public AggrDeviceEntity(){}
    public AggrDeviceEntity(AggrDeviceRepository deviceRepository, AggrDeviceSercetRepository deviceSercetRepository, AggrThingModelRepository thingModelRepository){
        this.deviceRepository = deviceRepository;
        this.deviceSercetRepository = deviceSercetRepository;
        this.thingModelRepository = thingModelRepository;

    }
    public AggrDeviceEntity(DeviceId deviceId,AggrDeviceRepository deviceRepository, AggrDeviceSercetRepository deviceSercetRepository, AggrThingModelRepository thingModelRepository){
        super(deviceId);
        this.deviceRepository = deviceRepository;
        this.deviceSercetRepository = deviceSercetRepository;
        this.thingModelRepository = thingModelRepository;
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

        // 获取设备对应的物模型，并且放入到设备聚合实体中
        Optional<DeviceThingCasePO> thingModelOptional = thingModelRepository.findThingModelByDeviceId(deviceId);

        // 数据实体转换为聚合
        AggrDeviceEntity aggrDeviceEntity = new AggrDeviceEntity();
        AggrDeviceSercetEntity aggrDeviceSercetEntity = new AggrDeviceSercetEntity();
        AggrThingEntity aggrThingEntity = new AggrThingEntity();

        aggrDeviceEntity.poToEntity(devicePO);
        aggrDeviceSercetEntity.poToEntity(deviceSercetPO);

        // 设备是有可能没有物模型的
        if(thingModelOptional.isPresent()){
            DeviceThingCasePO thingCasePO = thingModelOptional.get();
            aggrThingEntity.poToEntity(thingCasePO);
        }


        // 拼装聚合根
        aggrDeviceEntity.setDeviceSercetEntity(aggrDeviceSercetEntity);
        aggrDeviceEntity.setThingEntity(aggrThingEntity);

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
    public boolean updateDeviceStatus(DeviceStatusEnums deviceStatusEnums){
        boolean updateDeviceSercetStatus = deviceSercetRepository.updateDeviceStatus(this.getId(), deviceStatusEnums);
        boolean updateDeviceStatus = deviceRepository.updateDeviceStatus(this.getId(), deviceStatusEnums);

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

    /*
    修改自动激活状态
 */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAutoActive(String productKey, boolean autoActive){
        boolean updateDeviceSercetStatus = deviceSercetRepository.updateAutoActiveByProductKey(productKey, autoActive);

        if(updateDeviceSercetStatus){
            return true;
        }else{
            return false;
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean updateDeviceStatusAndOnlineTime(DeviceStatusEnums deviceStatusEnums, Long onlineTimestamp){
        /*
            1、判断是否可以自动激活
            2、如果可以自动激活
                2.1 直接改成Online或者offline的状态
            3、如果不能自动激活
                3.1 检查是否是Active的状态
                    3.1.1 如果不是， 则抛出异常
                    3.1.2 如果是， 则直接修改为Online或者offline的状态
         */
        boolean updateDeviceSercetStatus = deviceSercetRepository.updateDeviceStatus(this.getId(), deviceStatusEnums);
        boolean updateDeviceStatus =
                deviceRepository.updateDeviceStatusAndOnlineTime(
                        this.getId(), deviceStatusEnums, onlineTimestamp);

        if(updateDeviceSercetStatus && updateDeviceStatus){
            return true;
        }else{
            return false;
        }
    }

    public void saveThingModel(JsonElement thingModelJsonElement){
        // 解析需要入库的信息
        parseThingModel(thingModelJsonElement);
    }

    private void parseThingModel(JsonElement thingModelJsonElement) {
        String productKey = null;
        String schema = null;
        String profileJsonStr = null;
        String shadowJsonStr = null;
        String propertiesJsonStr = null;
        String eventsJsonStr = null;
        String servicesJsonStr = null;
        String thingJsonStr = thingModelJsonElement.toString();

        JsonObject thingModelJson = thingModelJsonElement.getAsJsonObject();
        // schema
        if(!thingModelJson.get("schema").isJsonNull()){
            schema = thingModelJson.get("schema").getAsString();
        }else{
            throw new AppException(RespCodeEnum.PARAM_IS_NULL.getCode(), JSON_FORMAT_ERROR);
        }

        // profile & productKey
        if(!thingModelJson.get("profile").isJsonNull()){
            JsonObject profileJson = thingModelJson.get("profile").getAsJsonObject();
            profileJsonStr = profileJson.toString();
            if(!profileJson.get("productKey").isJsonNull()){
                productKey = profileJson.get("productKey").getAsString();
            }else{
                throw new AppException(RespCodeEnum.PARAM_IS_NULL.getCode(), JSON_FORMAT_ERROR);
            }
        }else{
            throw new AppException(RespCodeEnum.PARAM_IS_NULL.getCode(), JSON_FORMAT_ERROR);
        }

        if(!thingModelJson.get("shadow").isJsonNull()){
            shadowJsonStr =  thingModelJson.get("shadow").toString();
        }

        if(!thingModelJson.get("properties").isJsonNull()){
            propertiesJsonStr =  thingModelJson.get("properties").toString();
        }

        if(!thingModelJson.get("events").isJsonNull()){
            eventsJsonStr =  thingModelJson.get("events").toString();
        }

        if(!thingModelJson.get("services").isJsonNull()){
            servicesJsonStr =  thingModelJson.get("services").toString();
        }

        // 将对应的信息入库
        DeviceThingCasePO po = new DeviceThingCasePO();
        po.setDeviceId(UUIDHelper.fromTimeUUID(this.getId().getUuid()));
        po.setSchemaStr(schema);
        po.setProfileJson(profileJsonStr);
        po.setProductKey(productKey);
        po.setShadowJson(shadowJsonStr);
        po.setPropertiesJson(propertiesJsonStr);
        po.setEventsJson(eventsJsonStr);
        po.setServicesJson(servicesJsonStr);
        po.setThingJson(thingJsonStr);

        thingModelRepository.saveThingModel(po);

    }


    public void poToEntity(DevicePO po){
        this.setId(new DeviceId(UUIDHelper.fromStringId(po.getUuid())));
        this.setTenantId(new TenantId(UUIDHelper.fromStringId(po.getTenantId())));
        this.setUserId(new UserId(UUIDHelper.fromStringId(po.getUserId())));
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
        if(po.getActiveTime() != null){
            this.setActiveTime(DateUtils.getTimestampByDate(po.getActiveTime()));
        }
        if(po.getLastOnlineTime() != null){
            this.setLastOnlineTime(DateUtils.getTimestampByDate(po.getLastOnlineTime()));
        }
    }

    public DevicePO entityToPO(){
        DevicePO devicePO = new DevicePO();
        devicePO.setUuid(UUIDHelper.fromTimeUUID(this.getId().getUuid()));
        devicePO.setUserId(UUIDHelper.fromTimeUUID(this.getUserId().getUuid()));
        devicePO.setTenantId(UUIDHelper.fromTimeUUID(this.getTenantId().getUuid()));
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
            devicePO.setActiveTime(DateUtils.getDateByTimestamp(this.getActiveTime()));
        }
        if(this.getLastOnlineTime() != null && this.getLastOnlineTime() != 0L){
            devicePO.setLastOnlineTime(DateUtils.getDateByTimestamp(this.getLastOnlineTime()));
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

    public TenantId getTenantId() {
        return tenantId;
    }

    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
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

    public AggrThingEntity getThingEntity() {
        return thingEntity;
    }

    public void setThingEntity(AggrThingEntity thingEntity) {
        this.thingEntity = thingEntity;
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
                ", thingEntity=" + thingEntity +
                ", lastOnlineTime=" + lastOnlineTime +
                ", deviceStatus=" + deviceStatus +
                ", sdkType='" + sdkType + '\'' +
                ", sdkVersion='" + sdkVersion + '\'' +
                ", deviceSercetEntity=" + deviceSercetEntity +
                '}';
    }
}
