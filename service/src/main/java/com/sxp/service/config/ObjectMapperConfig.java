package com.sxp.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 粟小蓬
 */
@Configuration
public class ObjectMapperConfig {

    @Bean
    ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }
}
