package com.casestudy.credit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server")})
public class SwaggerConfig {
    @Bean
    public OpenAPI springCreditAPI() {
        return new OpenAPI()
                .info(new Info().title("Spring Credit API")
                        .description("Spring application")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Credit Case Study Github Link")
                        .url("https://github.com/kadircan-yalniz/credit-case-study"));
    }
}
