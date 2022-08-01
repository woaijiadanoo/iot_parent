package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.extension.kafka;

import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.NodeConfiguration;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Collections;
import java.util.Map;

public class ExtensionKafkaNodeConfiguration implements NodeConfiguration<ExtensionKafkaNodeConfiguration> {

    private String topicPattern;
    private String bootstrapServers;
    private int retries;
    private int batchSize;
    private int linger;
    private int bufferMemory;
    private String acks;
    private String keySerializer;
    private String valueSerializer;
    private Map<String, String> otherProperties;

    private static final Integer DEFAULT_BATCH_SIZE=16384;
    private static final Integer DEFAULT_BUFFER_MEMORY=2*1024*1024;

    @Override
    public ExtensionKafkaNodeConfiguration defaultConfiguration() {
        ExtensionKafkaNodeConfiguration configuration = new ExtensionKafkaNodeConfiguration();
        configuration.setTopicPattern("ry-topic");
        configuration.setBootstrapServers("localhost:9092");
        configuration.setRetries(0);
        configuration.setBatchSize(DEFAULT_BATCH_SIZE);
        configuration.setLinger(0);
        configuration.setBufferMemory(DEFAULT_BUFFER_MEMORY);
        configuration.setAcks("-1");
        configuration.setKeySerializer(StringSerializer.class.getName());
        configuration.setValueSerializer(StringSerializer.class.getName());
        configuration.setOtherProperties(Collections.emptyMap());

        return configuration;
    }

    public String getTopicPattern() {
        return topicPattern;
    }

    public void setTopicPattern(String topicPattern) {
        this.topicPattern = topicPattern;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getLinger() {
        return linger;
    }

    public void setLinger(int linger) {
        this.linger = linger;
    }

    public int getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(int bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public Map<String, String> getOtherProperties() {
        return otherProperties;
    }

    public void setOtherProperties(Map<String, String> otherProperties) {
        this.otherProperties = otherProperties;
    }
}
