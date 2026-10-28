package com.banco.ms.cuentas.application.dto;



import java.time.LocalDateTime;

public record ClienteEventoMensaje(
        TipoEventoCliente tipo,
        String clienteId,
        String nombre,
        boolean estado,
        LocalDateTime fecha) {

    public enum TipoEventoCliente {
        CREADO,
        ACTUALIZADO,
        ELIMINADO
    }
}
