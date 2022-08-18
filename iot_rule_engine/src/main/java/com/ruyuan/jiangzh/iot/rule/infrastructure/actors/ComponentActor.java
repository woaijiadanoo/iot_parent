package com.ruyuan.jiangzh.iot.rule.infrastructure.actors;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextAwareActor;
import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.process.ComponentMsgProcessor;

public abstract class ComponentActor <T extends EntityId, P extends ComponentMsgProcessor> extends ContextAwareActor {
    private final TenantId tenantId;
    private final T entityId;

    private P processor;

    public ComponentActor(ActorSystemContext actorSystemContext, TenantId tenantId, T entityId) {
        super(actorSystemContext);
        this.tenantId = tenantId;
        this.entityId = entityId;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public T getEntityId() {
        return entityId;
    }

    public P getProcessor() {
        return processor;
    }

    public void setProcessor(P processor) {
        this.processor = processor;
    }
}
