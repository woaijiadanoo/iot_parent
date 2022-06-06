package com.ruyuan.jiangzh.service.sdk;

import com.ruyuan.jiangzh.service.dto.DeviceSercetDTO;

public interface DeviceServiceAPI {

    DeviceSercetDTO findDeviceBySercet(String productKey,String deviceName,String deviceSercet);

}
