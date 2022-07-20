package com.sxp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 粟小蓬
 * Swagger接口配置类
 *接口原生swagger启动方式：localhost:8080/swagger-ui.html
 * 接口ui界面启动方式：localhost:8080/doc.html
 */
@Configuration
@EnableSwagger2
public class SwaggerApiConfig {

    @Bean
    public Docket getDocket(){
//        创建封面信息对象
        ApiInfoBuilder apiInfoBuilder=new ApiInfoBuilder();
        apiInfoBuilder.version("2.0.1")
                .title("西米商城接口信息文档")
                .description("此信息页说明了西米商城接口规范及详情")
                .contact(new Contact("粟小蓬","https://gitee.com/xiaopengsu","3186885766@qq.com"));
        ApiInfo apiInfo=apiInfoBuilder.build();

        Docket docket=new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sxp.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;

    }
}
