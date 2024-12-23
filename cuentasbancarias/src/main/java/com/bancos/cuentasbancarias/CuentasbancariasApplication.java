package com.bancos.cuentasbancarias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.bancos.cuentasbancarias"})
public class CuentasbancariasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuentasbancariasApplication.class, args);
	}

}
