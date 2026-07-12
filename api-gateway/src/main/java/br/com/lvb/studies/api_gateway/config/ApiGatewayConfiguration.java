package br.com.lvb.studies.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator gatewayRouter(final RouteLocatorBuilder builder) {
        return builder.routes()
                    /*.route(p -> p.path("/get")
                            .filters(f -> f
                                    .addRequestHeader("Hello", "World")
                                    .addRequestParameter("new", "World"))
                            .uri("http://httpbin.org:80")
                    )*/
                    .route(p -> p.path("/book-service/**").uri("lb://book-service"))
                    .route(p -> p.path("/exchange-service/**").uri("lb://exchange-service"))
                .build();
    }
}
