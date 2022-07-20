package com.sxp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @MapperScan 需要导tkMapper下的包
 * @EnableScheduling 定时任务框架quartz，开启定时框架
 * @author 粟小蓬
 */
@SpringBootApplication
@MapperScan("com.sxp.dao")
@EnableScheduling
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }


}
