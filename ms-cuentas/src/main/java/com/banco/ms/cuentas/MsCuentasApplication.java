package com.banco.ms.cuentas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MsCuentasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCuentasApplication.class, args);
	}

}
