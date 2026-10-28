package com.banco.ms.clientes.domain.event;

import com.banco.ms.clientes.domain.model.Cliente;

import java.time.LocalDateTime;

public record ClienteEvento(TipoEventoCliente tipo,
                            String clienteId,
                            String nombre,
                            boolean estado,
                            LocalDateTime fecha) {

    public static ClienteEvento creado(Cliente cliente) {
        return de(TipoEventoCliente.CREADO, cliente);
    }

    public static ClienteEvento actualizado(Cliente cliente) {
        return de(TipoEventoCliente.ACTUALIZADO, cliente);
    }

    public static ClienteEvento eliminado(Cliente cliente) {
        return de(TipoEventoCliente.ELIMINADO, cliente);
    }

    private static ClienteEvento de(TipoEventoCliente tipo, Cliente cliente) {
        return new ClienteEvento(tipo, cliente.getClienteId(), cliente.getNombre(),
                cliente.isEstado(), LocalDateTime.now());
    }
}