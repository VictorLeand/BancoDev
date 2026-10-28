package com.banco.ms.clientes.infrastructure.identificador;

import com.banco.ms.clientes.domain.port.GeneradorClienteIdPort;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

@Component
public class UuidGeneradorClienteId implements GeneradorClienteIdPort {

    @Override
    public String generar() {
        return UUID.randomUUID().toString()
                .replace("-", "")
                .toUpperCase(Locale.ROOT);
    }
}