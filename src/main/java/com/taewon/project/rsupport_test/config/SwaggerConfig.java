package com.taewon.project.rsupport_test.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Rsupport Back-end API")
                .version("1.0.0")
                .description("""
                        Rsupport 백엔드 직군 입사 과제 API 문서입니다.
                        """)
                .termsOfService("http://swagger.io/terms/");

        return new OpenAPI()
                .info(info)
                .addServersItem(new Server().url("/rsupport/"));
    }

    @Bean
    public GroupedOpenApi announcementApi() {
        return GroupedOpenApi.builder()
                .group("Announcement API")
                .pathsToMatch("/rest/announcement/**")
                .build();
    }
}
