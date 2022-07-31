package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.extension.kafka;

import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineNode;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineNodeConfiguration;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineNodeUtils;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.RuleEngineContext;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.RuleEngineMsg;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ExtensionKafkaNode implements RuleEngineNode {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Producer<?, String> producer;

    private ExtensionKafkaNodeConfiguration kafkaConfig;

    @Override
    public void init(RuleEngineContext ctx, RuleEngineNodeConfiguration configuration) {
        // 将外部传入的configuration转换为Kafka的Config配置
        this.kafkaConfig = RuleEngineNodeUtils.convert(configuration, ExtensionKafkaNodeConfiguration.class);
        // 生成KafkaProducer所需的Properties配置
        Properties properties = new Properties();
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "producer-tb-kafka-node-" + ctx.getSelfId().getUuid().toString() + "-" + ctx.getNodeId());
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
