package com.minkyu.moais.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Moais 과제 Swagger")
                .description("지원자 김민규")
                .version("1.0.0");
    }

    // 모든 유저 가능한 API
    @Bean
    public GroupedOpenApi UserApi(){
        return GroupedOpenApi.builder()
                .group("1. Basic User")
                .packagesToScan("com.minkyu.moais.controller.notauth")
                .build();
    }

    // 로그인한 유저만 가능한 API
    @Bean
    public GroupedOpenApi AuthApi(){
        return GroupedOpenApi.builder()
                .group("2. Auth User")
                .packagesToScan("com.minkyu.moais.controller.auth")
                .addOpenApiCustomizer(customApiHeader())
                .build();
    }

    @Bean
    public OpenApiCustomizer customApiHeader() {
        Parameter jwtToken = new Parameter()
                .name("Authoriztion")
                .in("header")
                .description("'accessToken'을 넣으세요.")
                .required(true);

        return openApi -> openApi.getPaths().values().forEach(
                pathItem -> pathItem.readOperations().forEach(
                        operation -> operation.addParametersItem(jwtToken)
                )
        );
    }


}