package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

@TableName("device_thing_case")
public class DeviceThingCasePO extends Model<DeviceThingCasePO> {

    private static final long serialVersionUID = 1L;

    @TableId
    private String uuid;

    private String deviceId;

    private String productKey;

    private String schema;

    private String thingJson;

    private String profileJson;

    private String propertiesJson;

    private String eventsJson;

    private String servicesJson;

    private String shadowJson;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getThingJson() {
        return thingJson;
    }

    public void setThingJson(String thingJson) {
        this.thingJson = thingJson;
    }

    public String getProfileJson() {
        return profileJson;
    }

    public void setProfileJson(String profileJson) {
        this.profileJson = profileJson;
    }

    public String getPropertiesJson() {
        return propertiesJson;
    }

    public void setPropertiesJson(String propertiesJson) {
        this.propertiesJson = propertiesJson;
    }

    public String getEventsJson() {
        return eventsJson;
    }

    public void setEventsJson(String eventsJson) {
        this.eventsJson = eventsJson;
    }

    public String getServicesJson() {
        return servicesJson;
    }

    public void setServicesJson(String servicesJson) {
        this.servicesJson = servicesJson;
    }

    public String getShadowJson() {
        return shadowJson;
    }

    public void setShadowJson(String shadowJson) {
        this.shadowJson = shadowJson;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "DeviceThingCase{" +
        ", uuid=" + uuid +
        ", deviceId=" + deviceId +
        ", productKey=" + productKey +
        ", schema=" + schema +
        ", thingJson=" + thingJson +
        ", profileJson=" + profileJson +
        ", propertiesJson=" + propertiesJson +
        ", eventsJson=" + eventsJson +
        ", servicesJson=" + servicesJson +
        ", shadowJson=" + shadowJson +
        "}";
    }
}
