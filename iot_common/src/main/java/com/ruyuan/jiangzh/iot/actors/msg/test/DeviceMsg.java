package com.ruyuan.jiangzh.iot.actors.msg.test;

import com.ruyuan.jiangzh.iot.actors.msg.MsgType;
import com.ruyuan.jiangzh.iot.actors.msg.ServerAddress;
import com.ruyuan.jiangzh.iot.actors.msg.device.FromDeviceMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.ToDeviceActorMsg;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

import java.util.Optional;
import java.util.UUID;

public class DeviceMsg {

    public static ToDeviceActorMsg getToDeviceActorMsg(){
        ToDeviceActorMsg result = new ToDeviceActorMsg() {
            @Override
            public TenantId getTenantId() {
                String dataId = "1ecc89ff20cc080989e8b76480d43cf";
                UUID uuid = UUIDHelper.fromStringId(dataId);
                return new TenantId(uuid);
            }

            @Override
            public DeviceId getDeviceId() {
                String dataId = "1ecd4f30b230a7084c9bb52bf7b91c4";
                UUID uuid = UUIDHelper.fromStringId(dataId);
                return new DeviceId(uuid);
            }

            @Override
            public FromDeviceMsg getPayload() {
                FromDeviceMsg fromDeviceMsg = new FromDeviceMsg() {
                    @Override
                    public MsgType getMsgType() {
                        return MsgType.GET_ATTRIBUTE_REQUEST;
                    }
                };
                return fromDeviceMsg;
            }

            @Override
            public Optional<ServerAddress> getServerAddress() {
                return Optional.empty();
            }
        };
        return result;
    }

}
