package com.banco.ms.clientes.application.mapper;

import com.banco.ms.clientes.application.dto.ClienteResponse;
import com.banco.ms.clientes.domain.model.Cliente;

public final class ClienteMapper {

    private ClienteMapper() {
    }

    public static ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getClienteId(),
                cliente.getGenero(),
                cliente.getNombre(),
                cliente.getEdad(),
                cliente.getIdentificacion(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.isEstado());
    }
}
