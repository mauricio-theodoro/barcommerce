package com.barcommerce.barcommerce.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI barCommerceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BarCommerce API")
                        .description("API REST para gerenciar bar/restaurante")
                        .version("v1.0.0"))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório GitHub")
                        .url("https://github.com/mauricio-theodoro/barcommerce"));
    }
}
