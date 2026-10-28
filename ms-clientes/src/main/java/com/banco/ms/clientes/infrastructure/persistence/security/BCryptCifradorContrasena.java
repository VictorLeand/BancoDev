package com.banco.ms.clientes.infrastructure.persistence.security;

import com.banco.ms.clientes.domain.port.CifradorContrasenaPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptCifradorContrasena implements CifradorContrasenaPort {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String cifrar(String contrasenaPlana) {
        return encoder.encode(contrasenaPlana);
    }
}
