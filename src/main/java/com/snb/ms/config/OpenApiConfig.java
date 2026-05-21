package com.snb.ms.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Template API",
                version = "v1",
                description = "Microservice template API exposing company, salesman, admin user, and external posts endpoints. " +
                        "All endpoints support request context propagation using X-Request-Id, X-Tenant-Id, and Accept-Language headers.",
                contact = @Contact(
                        name = "MS Template Team",
                        email = "support@snb.com",
                        url = "https://snb.com/support"
                ),
                license = @License(name = "Proprietary", url = "https://snb.com/legal")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local development"),
                @Server(url = "/", description = "Relative deployment base path")
        },
        externalDocs = @ExternalDocumentation(
                description = "Project quick start and usage",
                url = "https://snb.com/docs/ms-template"
        )
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("MS Template API")
                        .version("v1")
                        .description("Detailed API contract for internal master data and external posts integration endpoints."))
                .components(new Components()
                        .addParameters("XRequestIdHeader", new Parameter()
                                .name("X-Request-Id")
                                .in("header")
                                .description("Correlation identifier for distributed tracing. If omitted, the service generates one.")
                                .required(false)
                                .schema(new StringSchema().example("req-20260504-0001")))
                        .addParameters("XTenantIdHeader", new Parameter()
                                .name("X-Tenant-Id")
                                .in("header")
                                .description("Tenant identifier used for multi-tenant context propagation.")
                                .required(false)
                                .schema(new StringSchema().example("tenant-001")))
                        .addParameters("AcceptLanguageHeader", new Parameter()
                                .name("Accept-Language")
                                .in("header")
                                .description("Preferred locale for localized validation and error messages.")
                                .required(false)
                                .schema(new StringSchema().example("en"))))
                .externalDocs(new io.swagger.v3.oas.models.ExternalDocumentation()
                        .description("MS Template reference documentation")
                        .url("https://snb.com/docs/ms-template"));
    }
}
