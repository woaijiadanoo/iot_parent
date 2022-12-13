package com.ruyuan.jiangzh.iot.device.application.appservice;

import com.ruyuan.jiangzh.service.dto.ThingModelTransportDTO;
import com.ruyuan.jiangzh.service.sdk.ThingModelServiceAPI;
import org.springframework.stereotype.Component;

@Component
public class ThingModelServiceImpl implements ThingModelServiceAPI {


    @Override
    public String transportToThingModel(ThingModelTransportDTO dto) {
        return null;
    }

}
