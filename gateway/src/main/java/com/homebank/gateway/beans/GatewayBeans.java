package com.homebank.gateway.beans;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GatewayBeans {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){

        return  builder
                .routes()
                .route(route->route
                        .path("/cuenta-bancaria/api/account/**")
                        .uri("http://localhost:8081"))
                .route(route->route
                        .path("/cuenta-bancaria/api/client/**")
                        .uri("http://localhost:8081"))
                .route(route->route
                        .path("/cuenta-bancaria/api/accountType/**")
                        .uri("http://localhost:8081"))
                .route(route->route
                        .path("/cuenta-bancaria/api/clientType/**")
                        .uri("http://localhost:8081"))
                .route(route->route
                        .path("/cuenta-bancaria/api/holder/**")
                        .uri("http://localhost:8081"))
                .route(route->route
                        .path("/cuenta-bancaria/api/signature/**")
                        .uri("http://localhost:8081"))
                .route(route->route
                        .path("/cuenta-bancaria/api/creditType/**")
                        .uri("http://localhost:8081"))
                .route(route->route
                        .path("/cuenta-bancaria/api/credit/**")
                        .uri("http://localhost:8081"))
                .route(route->route
                        .path("/cuenta-bancaria/api/movement/**")
                        .uri("http://localhost:8081"))
                .route(route->route
                        .path("/cuenta-bancaria/api/pay/**")
                        .uri("http://localhost:8081"))
                .route(route->route
                        .path("/cuenta-bancaria/api/consumption/**")
                        .uri("http://localhost:8081"))
                .route(route->route
                        .path("/cuenta-bancaria/api/creditCard/**")
                        .uri("http://localhost:8081"))
                .build();
    }

}
