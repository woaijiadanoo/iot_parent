package com.ruyuan.jiangzh.protol.infrastructure.demo;

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
public class Bucket4jTest {

    public static void main(String[] args) throws InterruptedException {
        String limitConfig = "100:1,300:10";

        Bucket4jTest test = new Bucket4jTest(limitConfig);

        int i = 1;
        while (true) {
            if(test.tryConsume()){
                System.out.println("true i = " + i);
            }else{
                System.err.println("false i = " + i);
            }

            i++;

            if(i % 100 == 0){
                Thread.sleep(1000L);
            }

            if(i >= 500){
                break;
            }
        }

    }

    private final LocalBucket localBucket;

    public Bucket4jTest(String limitConfig){
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
