package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity;

import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.device.ProductId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.UserId;
import com.ruyuan.jiangzh.iot.common.DateUtils;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DevicePO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceThingCasePO;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceStatusEnums;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceTypeEnums;

import java.io.Serializable;

public class AggrThingEntity implements Serializable {

    private String shadowJsonStr;

    private String propertiesJsonStr;

    private String eventsJsonStr;

    private String servicesJsonStr;

    public AggrThingEntity(){}


    public AggrThingEntity(
            String shadowJsonStr, String propertiesJsonStr, String eventsJsonStr, String servicesJsonStr) {
        this.shadowJsonStr = shadowJsonStr;
        this.propertiesJsonStr = propertiesJsonStr;
        this.eventsJsonStr = eventsJsonStr;
        this.servicesJsonStr = servicesJsonStr;
    }

    public void poToEntity(DeviceThingCasePO po){
        AggrThingEntity aggrThingEntity = new AggrThingEntity();
        aggrThingEntity.setShadowJsonStr(po.getShadowJson());
        aggrThingEntity.setPropertiesJsonStr(po.getPropertiesJson());
        aggrThingEntity.setEventsJsonStr(po.getEventsJson());
        aggrThingEntity.setServicesJsonStr(po.getServicesJson());
    }

    public String getShadowJsonStr() {
        return shadowJsonStr;
    }

    public void setShadowJsonStr(String shadowJsonStr) {
        this.shadowJsonStr = shadowJsonStr;
    }

    public String getPropertiesJsonStr() {
        return propertiesJsonStr;
    }

    public void setPropertiesJsonStr(String propertiesJsonStr) {
        this.propertiesJsonStr = propertiesJsonStr;
    }

    public String getEventsJsonStr() {
        return eventsJsonStr;
    }

    public void setEventsJsonStr(String eventsJsonStr) {
        this.eventsJsonStr = eventsJsonStr;
    }

    public String getServicesJsonStr() {
        return servicesJsonStr;
    }

    public void setServicesJsonStr(String servicesJsonStr) {
        this.servicesJsonStr = servicesJsonStr;
    }

    @Override
    public String toString() {
        return "AggrThingEntity{" +
                "shadowJsonStr='" + shadowJsonStr + '\'' +
                ", propertiesJsonStr='" + propertiesJsonStr + '\'' +
                ", eventsJsonStr='" + eventsJsonStr + '\'' +
                ", servicesJsonStr='" + servicesJsonStr + '\'' +
                '}';
    }
}
