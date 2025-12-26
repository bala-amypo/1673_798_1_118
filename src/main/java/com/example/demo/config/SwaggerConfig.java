package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Supply Chain Management API")
                        .version("1.0")
                        .description("API for managing suppliers, purchase orders, deliveries, and risk alerts"))
                .addServersItem(new Server()
                        .url("https://9198.pro604cr.amypo.ai")
                        .description(""))
    };
}