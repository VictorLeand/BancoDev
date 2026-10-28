package com.banco.ms.clientes.application.dto;

import com.banco.ms.clientes.domain.model.Genero;

public record ClienteResponse(
        String clienteId,
        Genero genero,
        String nombre,
        Integer edad,
        String identificacion,
        String direccion,
        String telefono,
        boolean estado) {
}
