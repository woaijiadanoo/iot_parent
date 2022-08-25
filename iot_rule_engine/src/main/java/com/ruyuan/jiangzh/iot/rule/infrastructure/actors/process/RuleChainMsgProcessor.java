package com.ruyuan.jiangzh.iot.rule.infrastructure.actors.process;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.google.common.collect.Maps;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.msg.IoTMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.ServiceToRuleEngineMsg;
import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.EntityType;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity.RuleChainMetaDataEntity;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity.RuleNodeEntity;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.vo.EntityRelationVO;
import com.ruyuan.jiangzh.iot.rule.domain.domainservice.RuleChainDomainService;
import com.ruyuan.jiangzh.iot.rule.domain.entity.RuleChainEntity;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.ComponentState;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.RuleEngineActorSystemContext;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.RuleNodeActor;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.messages.RuleChainToRuleNodeMsg;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.messages.RuleNodeToRuleChainTellNextMsg;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.DefaultRuleEngineContext;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.RuleEngineContext;

import java.util.*;
import java.util.stream.Collectors;

public class RuleChainMsgProcessor extends ComponentMsgProcessor<RuleChainId>{

    private final RuleChainDomainService ruleChainDomainService;

    /*
        ruleChain的关键属性
     */
    private RuleNodeId firstId;
    private RuleNodeContext firstNode;
    private boolean started;
    private String ruleChainName;
    private final ActorRef parent;
    private final ActorRef self;

    // node的列表
    private final Map<RuleNodeId, RuleNodeContext> nodeActors;

    // node之间的关系
    private final Map<RuleNodeId, List<RuleNodeRelation>> nodeRoutes;

    public RuleChainMsgProcessor(
            ActorSystemContext systemContext, TenantId tenantId, RuleChainId entityId, ActorRef parent, ActorRef self) {
        super(systemContext, tenantId, entityId);
        this.parent = parent;
        this.self = self;
        this.nodeActors = Maps.newHashMap();
        this.nodeRoutes = Maps.newHashMap();
        if(systemContext instanceof RuleEngineActorSystemContext){
            RuleEngineActorSystemContext actorSystemContext = (RuleEngineActorSystemContext) systemContext;
            this.ruleChainDomainService = actorSystemContext.getRuleChainService();
        }else{
            this.ruleChainDomainService = null;
        }
    }

    @Override
    public void start(ActorContext context) throws Exception {
        if(!started){
            RuleChainEntity ruleChain = ruleChainDomainService.findRuleChainById(tenantId, entityId);
            RuleChainMetaDataEntity metaDataEntity = ruleChainDomainService.loadRuleChainMetaData(tenantId, entityId);
            if(ruleChain != null && metaDataEntity != null){
                ruleChainName = ruleChain.getRuleChainName();
                List<RuleNodeEntity> nodes = metaDataEntity.getNodes();
                for(RuleNodeEntity node : nodes){
                    ActorRef ruleNodeActor = createRuleNodeActor(context, node);
                    nodeActors.put(node.getId(), new RuleNodeContext(tenantId, self, ruleNodeActor, node));
                }
                initRoutes(ruleChain, metaDataEntity, nodes);
                started = true;
            }
        } else {
            onUpdate(context);
        }
    }

    private void initRoutes(RuleChainEntity ruleChain, RuleChainMetaDataEntity metaDataEntity, List<RuleNodeEntity> nodes) {
        nodeRoutes.clear();
        for(RuleNodeEntity node : nodes){
            List<EntityRelationVO> relations = metaDataEntity.getRuleNodeRelations(node.getId());
            if(relations == null || relations.size() == 0){
                nodeRoutes.put(node.getId(), Collections.emptyList());
            }else{
                for(EntityRelationVO relation : relations){
                    if(relation.getTo().getEntityType() == EntityType.RULE_NODE){
                        RuleNodeContext ruleNodeCtx = nodeActors.get(new RuleNodeId(relation.getTo().getUuid()));
                        if(ruleNodeCtx == null){
                            throw new RuntimeException("no ruleNode : ["+node.getId()+"]");
                        }
                        nodeRoutes.computeIfAbsent(
                                node.getId(),
                                k-> new ArrayList<>()).add(new RuleNodeRelation(node.getId(), relation.getTo(), relation.getType()));
                    }
                }
            }
        }
        firstId = ruleChain.getFirstRuleNodeId();
        firstNode = nodeActors.get(firstId);
        state = ComponentState.ACTIVE;
    }

    /*
        创建或者RuleNodeActor
     */
    private ActorRef createRuleNodeActor(ActorContext context, RuleNodeEntity node) {
        String dispatcherName = "rule-node-dispatcher";
        return context.actorOf(
                Props.create(
                        new RuleNodeActor.ActorCreator(systemContext, tenantId, entityId, node.getId()))
                        .withDispatcher(dispatcherName), node.getId().toString());
    }

    @Override
    public void stop(ActorContext context) throws Exception {
        nodeActors.values().stream().map(RuleNodeContext::getSelfActor).forEach(context::stop);
        nodeActors.clear();
        nodeRoutes.clear();
        started = false;
    }

    @Override
    public String componentName() throws Exception {
        return this.ruleChainName;
    }

    public void onServiceToRuleEngineMsg(ServiceToRuleEngineMsg msg) {
        checkActive();
        if(firstNode != null){
            pushMsgToNode(firstNode, getIoTMsgByRuleChain(msg.getMsg()),  "");
        }
    }

    /*
        获取一个新的IoTMsg， 避免引用原始对象
     */
    private IoTMsg getIoTMsgByRuleChain(IoTMsg msg) {
        return new IoTMsg(msg.getId(), msg.getType(), msg.getOriginator(), msg.getData(), entityId, null);
    }

    /*
        将消息推送给RuleNodeActor
     */
    private void pushMsgToNode(RuleNodeContext nodeCtx, IoTMsg msg, String fromRelationType){
        if(nodeCtx != null){
            RuleEngineContext ruleEngineCtx = new DefaultRuleEngineContext(systemContext, nodeCtx);
            // 给nodeActor投递消息
            nodeCtx.getSelfActor().tell(new RuleChainToRuleNodeMsg(ruleEngineCtx, msg, fromRelationType), self);
        }
    }

    public void onRuleNodeToRuleChainTellNextMsg(RuleNodeToRuleChainTellNextMsg msg) {
        checkActive();

        onLocalTellNext(msg);
    }

    /*
        ry_kafka_input
            success -> ry_kafka_success_01
            success -> ry_kafka_success_02
            failture -> ry_kafka_failture
     */
    private void onLocalTellNext(RuleNodeToRuleChainTellNextMsg tellNextMsg) {
        IoTMsg msg = tellNextMsg.getMsg();
        RuleNodeId originatorNodeId = tellNextMsg.getOriginator();
        // 筛选需要的relationType
        List<RuleNodeRelation> relations = nodeRoutes.get(originatorNodeId).stream()
                .filter(r -> sameRelationType(tellNextMsg.getRelationTypes(), r.getType()))
                .collect(Collectors.toList());

        int relationsCount = relations.size();
        if(relationsCount == 0){
            // 这个流程到这就截止了
        } else if (relationsCount == 1) {
            for(RuleNodeRelation relation : relations){
                pushToTarget(msg, relation.getOut(), relation.getType());
            }
        } else {
            for(RuleNodeRelation relation : relations){
                EntityId target = relation.getOut();
                switch (target.getEntityType()){
                    case RULE_NODE:
                        copyMsgToNode(msg, target, relation.getType());
                        break;
                    default:
                        // 目前没有什么处理的内容
                }
            }
        }

    }

    private void copyMsgToNode(IoTMsg msg, EntityId target, String fromRelationType) {
        RuleNodeId targetId = new RuleNodeId(target.getUuid());
        RuleNodeContext targetNodeCtx = nodeActors.get(targetId);
        // 主要怕多个ruleNode之间的消息修改会影响其他的ruleNode
        IoTMsg targetMsg = msg.copy(UUIDHelper.genUuid(), entityId, targetId);
        pushMsgToNode(targetNodeCtx, targetMsg, fromRelationType);
    }

    private void pushToTarget(IoTMsg msg, EntityId target, String fromRelationType) {
        switch (target.getEntityType()){
            case RULE_NODE:
                pushMsgToNode(nodeActors.get(new RuleNodeId(target.getUuid())), msg, fromRelationType);
                break;
            default:
                // 目前没有什么处理的内容
        }
    }

    private boolean sameRelationType(Set<String> relationTypes, String type) {
        if(relationTypes == null){
            return true;
        }
        for(String relationType : relationTypes){
            if(relationType.equalsIgnoreCase(type)){
                return true;
            }
        }
        return false;
    }


}
