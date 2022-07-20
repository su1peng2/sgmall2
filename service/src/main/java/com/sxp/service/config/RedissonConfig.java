package com.sxp.service.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 粟小蓬
 */
@Configuration
public class RedissonConfig {
    @Value("${redisson.addr.singleAddr.host}")
    private String host;

    @Value("${redisson.addr.singleAddr.database}")
    private int database;


    @Bean
    public RedissonClient redissonClient(){
        System.out.println(host+"----"+database);
        Config config=new Config();
        config.useSingleServer().setAddress(host).setPassword("xiaopengroot123").setDatabase(database);
        return Redisson.create(config);
    }
}
