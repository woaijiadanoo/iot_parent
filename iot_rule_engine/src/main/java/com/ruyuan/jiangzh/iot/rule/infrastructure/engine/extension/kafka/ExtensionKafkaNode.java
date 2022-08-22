package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.extension.kafka;

import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineNode;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineNodeConfiguration;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineNodeUtils;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineRelationTypes;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.RuleEngineContext;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.RuleEngineMsg;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.RuleEngineMsgMetaData;
import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ExtensionKafkaNode implements RuleEngineNode {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // kafka 配置名称
    private static final String OFFSET="offset";
    private static final String PARTITION = "partition";
    private static final String TOPIC = "topic";
    private static final String ERROR = "error";


    private Producer<?, String> producer;

    private ExtensionKafkaNodeConfiguration kafkaConfig;

    @Override
    public void init(RuleEngineContext ctx, RuleEngineNodeConfiguration configuration) {
        // 将外部传入的configuration转换为Kafka的Config配置
        this.kafkaConfig = RuleEngineNodeUtils.convert(configuration, ExtensionKafkaNodeConfiguration.class);
        // 生成KafkaProducer所需的Properties配置
        Properties properties = new Properties();
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "producer-kafka-node-" + ctx.getSelfId().getUuid().toString() + "-" + ctx.getNodeId());
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaConfig.getValueSerializer());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaConfig.getKeySerializer());
        properties.put(ProducerConfig.ACKS_CONFIG, kafkaConfig.getAcks());
        properties.put(ProducerConfig.RETRIES_CONFIG, kafkaConfig.getRetries());
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaConfig.getBatchSize());
        properties.put(ProducerConfig.LINGER_MS_CONFIG, kafkaConfig.getLinger());
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaConfig.getBufferMemory());
        if (kafkaConfig.getOtherProperties() != null) {
            kafkaConfig.getOtherProperties()
                    .forEach((k,v) -> properties.put(k, v));
        }

        try {
            // 创建KafkaProducer
            this.producer = new KafkaProducer<>(properties);
        } catch (Exception e) {
            logger.error("ExtensionKafkaNode producer create failture , properties: [{}]", properties);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onMsg(RuleEngineContext ctx, RuleEngineMsg msg) {
        // 确定topic [  ry_topic |  ${name}_topic ]
        String topic = RuleEngineNodeUtils.processPattern(kafkaConfig.getTopicPattern(), msg.getMetaData());
        try {
            // 通过producer.send推送消息至Kafka
            producer.send(new ProducerRecord<>(topic, msg.getData()), (metadata, e) -> {
                if(metadata != null){
                    // 成功的下一步处理
                    RuleEngineMsg nextMsg = processResponse(ctx, msg, metadata);
                    ctx.tellNext(nextMsg, RuleEngineRelationTypes.SUCCESS);
                } else {
                    // 失败的下一步处理
                    RuleEngineMsg nextMsg = processException(ctx, msg, e);
                    ctx.tellFailure(nextMsg, e);
                }
            });

        } catch (Exception e){
            // 如果失败了，应该像办法处理失败
            ctx.tellFailure(msg, e);
        }
    }

    private RuleEngineMsg processResponse(RuleEngineContext ctx, RuleEngineMsg origMsg, RecordMetadata recordMetadata){
        RuleEngineMsgMetaData metaData = origMsg.getMetaData().copy();
        metaData.putValue(OFFSET, recordMetadata.offset()+"");
        metaData.putValue(PARTITION, recordMetadata.partition()+"");
        metaData.putValue(TOPIC, recordMetadata.topic());

        return ctx.transformMsg(origMsg, origMsg.getType(), origMsg.getOriginator(), metaData, origMsg.getData());
    }

    private RuleEngineMsg processException(RuleEngineContext ctx, RuleEngineMsg origMsg, Exception e){
        RuleEngineMsgMetaData metaData = origMsg.getMetaData().copy();

        metaData.putValue(ERROR, e.getClass() + " : " + e.getMessage());

        return ctx.transformMsg(origMsg, origMsg.getType(), origMsg.getOriginator(), metaData, origMsg.getData());
    }

    @Override
    public void destory() {
        if(this.producer != null){
            try {
                this.producer.close();
            } catch (Exception e){
                logger.error("ExtensionKafkaNode producer destory failture");
                throw new RuntimeException(e);
            }
        }
    }
}
