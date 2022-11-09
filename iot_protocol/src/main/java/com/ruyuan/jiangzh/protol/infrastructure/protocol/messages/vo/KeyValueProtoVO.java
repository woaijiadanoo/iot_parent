package com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.vo;

public class KeyValueProtoVO {
    private String key;

    private KeyValueType type;

    private Boolean boolValue;

    private Long longValue;

    private Double doubleValue;

    private String stringValue;

    public KeyValueProtoVO(NewBuilder newBuilder){
        this.key = newBuilder.key;
        this.type = newBuilder.type;
        this.boolValue = newBuilder.boolValue;
        this.longValue = newBuilder.longValue;
        this.doubleValue = newBuilder.doubleValue;
        this.stringValue = newBuilder.stringValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public KeyValueType getType() {
        return type;
    }

    public void setType(KeyValueType type) {
        this.type = type;
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public void setBoolValue(Boolean boolValue) {
        this.boolValue = boolValue;
    }

    public Long getLongValue() {
        return longValue;
    }

    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public static class NewBuilder {

        private String key;

        private KeyValueType type;

        private Boolean boolValue;

        private Long longValue;

        private Double doubleValue;

        private String stringValue;


        public NewBuilder setKey(String key) {
            this.key = key;
            return this;
        }

        public NewBuilder setType(KeyValueType type) {
            this.type = type;
            return this;
        }

        public NewBuilder setBoolValue(Boolean boolValue) {
            this.boolValue = boolValue;
            return this;
        }

        public NewBuilder setLongValue(Long longValue) {
            this.longValue = longValue;
            return this;
        }

        public NewBuilder setDoubleValue(Double doubleValue) {
            this.doubleValue = doubleValue;
            return this;
        }

        public NewBuilder setStringValue(String stringValue) {
            this.stringValue = stringValue;
            return this;
        }

        public KeyValueProtoVO build(){
            return new KeyValueProtoVO(this);
        }
    }


    @Override
    public String toString() {
        return "KeyValueProtoVO{" +
                "key='" + key + '\'' +
                ", type=" + type +
                ", boolValue=" + boolValue +
                ", longValue=" + longValue +
                ", doubleValue=" + doubleValue +
                ", stringValue='" + stringValue + '\'' +
                '}';
    }
}
