package com.clairtonluz.sigmatest.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Sigma Test API", version = "v1"))
public class OpenAPIConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI();
    }
}
