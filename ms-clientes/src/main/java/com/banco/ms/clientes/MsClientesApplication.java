package com.banco.ms.clientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MsClientesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsClientesApplication.class, args);
	}

}
