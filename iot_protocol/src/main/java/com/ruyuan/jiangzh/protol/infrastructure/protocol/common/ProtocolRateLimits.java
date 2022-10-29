package com.ruyuan.jiangzh.protol.infrastructure.protocol.common;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.local.LocalBucket;
import io.github.bucket4j.local.LocalBucketBuilder;

import java.time.Duration;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class ProtocolRateLimits {

    private final LocalBucket localBucket;

    public ProtocolRateLimits(String limitConfig){
        LocalBucketBuilder builder = Bucket4j.builder();
        boolean initialized = false;
        for(String limitSrc : limitConfig.split(",")){
            long capacity = Long.parseLong(limitSrc.split(":")[0]);
            long duration = Long.parseLong(limitSrc.split(":")[1]);

            builder.addLimit(Bandwidth.simple(capacity, Duration.ofSeconds(duration)));

            initialized = true;
        }

        if(initialized){
            localBucket = builder.build();
        }else{
            throw new IllegalArgumentException("Failed to parse limit config : "+limitConfig);
        }
    }

    public boolean tryConsume(){
        return localBucket.tryConsume(1);
    }

}
