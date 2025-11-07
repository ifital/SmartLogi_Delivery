package com.smart_delivery_management.smartlogi_delivery.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Delivery Management System API")
                        .version("1.0.0")
                        .description("Documentation interactive des endpoints du système de gestion logistique SmartLogi")
                        .contact(new Contact()
                                .name("Équipe SmartLogi")
                                .email("support@smartlogi.com")));
    }
}
