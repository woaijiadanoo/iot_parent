package com.ruyuan.jiangzh.iot.device.infrastructure.actors;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.app.AppActor;

public class DeviceAppActor extends AppActor {

    public DeviceAppActor(ActorSystemContext actorSystemContext){
        super(actorSystemContext);
    }

    @Override
    public void doStart() {

    }

    @Override
    public void doReceive(Object msg) {

    }

    public final class ActorCreator extends ContextBaseCreator<DeviceAppActor>{

        public ActorCreator(ActorSystemContext actorSystemContext){
            super(actorSystemContext);
        }

        @Override
        public DeviceAppActor create() throws Exception {
            return new DeviceAppActor(actorSystemContext);
        }
    }

}
