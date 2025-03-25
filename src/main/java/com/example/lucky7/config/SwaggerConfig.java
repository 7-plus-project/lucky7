package com.example.lucky7.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@SecurityScheme(
        type = SecuritySchemeType.APIKEY,
        name = "Authorization",
        description = "access Token을 입력해주세요.",
        in = SecuritySchemeIn.HEADER)

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .security(Collections.singletonList(new SecurityRequirement().addList("Authorization")));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        String[] packages = {"com.example.lucky7"};
        return GroupedOpenApi.builder()
                .group("default")
                .packagesToScan(packages)
                .build();
    }

    private Info apiInfo() {
        String description = "안녕하세요";
        return new Info()
                .title("Lucky 7")
                .description(description)
                .version("1");
    }
}