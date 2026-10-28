package com.banco.ms.cuentas.domain.model;


import java.time.LocalDateTime;

public record ClienteSnapshot(String clienteId, String nombre, boolean estado, LocalDateTime fechaActualizacion) {

    public boolean esPosteriorA(LocalDateTime fechaEvento) {
        return fechaActualizacion.isAfter(fechaEvento);
    }
}
