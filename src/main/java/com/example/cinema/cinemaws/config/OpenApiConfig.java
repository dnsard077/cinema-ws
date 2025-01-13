package com.example.cinema.cinemaws.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Example Cinema API Documentation")
                        .version("1.0")
                        .description("This is an Open API Documentation for Example Cinema"))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            StringSchema acceptLanguageSchema = new StringSchema()
                    .addEnumItem("en")
                    .addEnumItem("id");
            Parameter acceptLanguageHeader = new Parameter()
                    .name("Accept-Language")
                    .description("Language for response translation")
                    .required(false)
                    .in(ParameterIn.HEADER.toString())
                    .schema(acceptLanguageSchema);

            Parameter ifNoneMatchHeader = new Parameter()
                    .name("If-None-Match")
                    .description("ETag value for conditional requests (used for cache validation)")
                    .required(false)
                    .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema());

            operation.addParametersItem(acceptLanguageHeader);
            operation.addParametersItem(ifNoneMatchHeader);
            return operation;
        };
    }
}
