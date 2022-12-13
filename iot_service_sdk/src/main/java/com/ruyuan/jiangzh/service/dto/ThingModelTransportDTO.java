package com.ruyuan.jiangzh.service.dto;

import com.ruyuan.jiangzh.iot.actors.msg.rule.vo.KeyValueProtoVO;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;

import java.io.Serializable;
import java.util.List;

public class ThingModelTransportDTO implements Serializable {

    private DeviceId deviceId;

    private int thingModelTypeCode;

    private List<KeyValueProtoVO> kvs;

    public ThingModelTransportDTO(){}

    public ThingModelTransportDTO(DeviceId deviceId, int thingModelTypeCode, List<KeyValueProtoVO> kvs) {
        this.deviceId = deviceId;
        this.thingModelTypeCode = thingModelTypeCode;
        this.kvs = kvs;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }


    public int getThingModelTypeCode() {
        return thingModelTypeCode;
    }

    public List<KeyValueProtoVO> getKvs() {
        return kvs;
    }

}
