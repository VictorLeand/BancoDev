package com.banco.ms.clientes.infrastructure.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "mensajeria")
public record MensajeriaProperties(@NotBlank String exchange) {
}
