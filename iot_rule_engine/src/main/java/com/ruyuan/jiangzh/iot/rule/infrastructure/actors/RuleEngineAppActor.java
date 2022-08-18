package com.ruyuan.jiangzh.iot.rule.infrastructure.actors;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.app.AppActor;
import com.ruyuan.jiangzh.service.sdk.TenantServiceAPI;

public class RuleEngineAppActor  extends AppActor {

    public RuleEngineAppActor(ActorSystemContext actorSystemContext) {
        super(actorSystemContext);
    }

    @Override
    public void doStart() {

    }

    @Override
    public void doReceive(Object msg) {

    }

    public static class ActorCreator extends ContextBaseCreator<RuleEngineAppActor> {
        public ActorCreator(ActorSystemContext actorSystemContext){
            super(actorSystemContext);
        }

        @Override
        public RuleEngineAppActor create() throws Exception {
            return new RuleEngineAppActor(actorSystemContext);
        }
    }

}
