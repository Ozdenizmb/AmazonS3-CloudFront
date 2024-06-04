package com.amazon.asset.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI utilityOpenAPI(
            @Value("${openapi.app.description}") String description,
            @Value("${openapi.app.version}") String version,
            @Value("${openapi.app.title}") String title,
            @Value("${openapi.app.contact}") String contactName,
            @Value("${openapi.app.email}") String email,
            @Value("${openapi.app.licence}") String licenseName,
            @Value("${openapi.app.url}") String url
    ) {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title(title).description(description).version(version)
                        .contact(new Contact().name(contactName).email(email))
                        .license(new License().name(licenseName).url(url))
                );
    }
}
