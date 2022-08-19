package com.ruyuan.jiangzh.iot.rule.infrastructure.actors.process;

import akka.actor.ActorContext;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.ComponentState;

public abstract class ComponentMsgProcessor<T extends EntityId> extends AbstractMsgProcessor {

    protected final TenantId tenantId;
    protected final T entityId;
    protected ComponentState state;

    protected ComponentMsgProcessor(ActorSystemContext systemContext,TenantId tenantId, T entityId) {
        super(systemContext);
        this.tenantId = tenantId;
        this.entityId = entityId;
    }

    // processor启动
    public abstract void start(ActorContext context) throws Exception;
    // processor停止
    public abstract void stop(ActorContext context) throws Exception;
    // processor 修改时的触发动作
    public abstract void onUpdate(ActorContext context) throws Exception;

    // processor componentName
    public abstract String componentName() throws Exception;

}
