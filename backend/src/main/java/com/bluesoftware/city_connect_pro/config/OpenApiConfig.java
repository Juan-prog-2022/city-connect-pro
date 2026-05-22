package com.bluesoftware.city_connect_pro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    private static final String BASIC_AUTH = "basicAuth";

    @Bean
    OpenAPI cityConnectOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("City Connect Pro API")
                        .description("API para conectar profesionales locales con clientes")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(BASIC_AUTH))
                .components(new Components()
                        .addSecuritySchemes(BASIC_AUTH, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")));
    }
}
