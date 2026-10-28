package com.banco.ms.cuentas.infrastructure.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "mensajeria")
public record MensajeriaProperties(@NotBlank String exchange, @NotBlank String cola) {

    public String exchangeErrores() {
        return exchange + ".dlx";
    }

    public String colaErrores() {
        return cola + ".dlq";
    }
}
