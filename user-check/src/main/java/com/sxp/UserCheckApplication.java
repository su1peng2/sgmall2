package com.sxp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.sxp.dao")
@EnableEurekaClient
public class UserCheckApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCheckApplication.class, args);
    }

}
