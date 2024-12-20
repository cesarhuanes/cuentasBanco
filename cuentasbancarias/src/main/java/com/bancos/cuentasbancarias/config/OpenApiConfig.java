package com.bancos.cuentasbancarias.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info=@Info(title = "Cuentas Bancarias",
            version = "1.0",
            description = "Documentacion de endpoint en Cuentas")
)
public class OpenApiConfig {
}
