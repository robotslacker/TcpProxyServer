package com.robotslacker.tcpproxy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author robotslacker
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 读取配置文件中的enable，true为显示，false为隐藏
    @Value("${swagger.enable}")
    private boolean enable;

    @Value("${server.port}")
    private int port;

    @Bean
    public Docket createRestApi() {
        if (enable) {
            logger.info("Started swagger ui http://localhost:" + port + "/swagger-ui/index.html");
        } else {
            logger.info("Swagger ui disabled.");
        }
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Tag.class))
                .paths(PathSelectors.any())
                .build()
                .enable(enable);
    }

    // 创建api的基本信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Ares云平台服务")
                .description("Ares cloud service.")
                .version("4.0")
                .build();
    }
}