package com.sxp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableHystrix
@EnableFeignClients
@EnableEurekaClient
public class ApiUserRegistApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiUserRegistApplication.class, args);
    }

}
