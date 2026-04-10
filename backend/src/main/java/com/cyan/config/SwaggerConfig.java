package com.cyan.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT bearer token authentication"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI cyanStreamingOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cyan Streaming API")
                        .description("REST API for a movie streaming platform")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Cyan")
                                .email("cyan@example.com")));
    }
}
