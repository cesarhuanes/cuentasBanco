package com.bancos.cuentasbancarias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.bancos.cuentasbancarias"})
public class CuentasbancariasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuentasbancariasApplication.class, args);
	}

}
