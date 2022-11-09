package com.ruyuan.jiangzh.protol.infrastructure.configs;

import com.ruyuan.jiangzh.protol.infrastructure.protocol.common.JsonConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConverterConfig {

    @Value("${protocol.json.max_string_value_length}")
    public void setMaxStringValueLength(int maxStringValueLength){
        JsonConverter.setMaxStringValueLength(maxStringValueLength);
    }

}
