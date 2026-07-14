package br.com.lvb.studies.exchange_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(info = @Info(
        title = "Exchange Microservice API",
        version = "V1",
        description = "Documentation of Exchange Microservice API")
)
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Exchange Microservice API")
                        .version("V1")
                        .license(new License().name("Apache 2.0").url("https://www.linkedin.com/in/veloso-lucas/?locale=en"))
                );
    }

}
