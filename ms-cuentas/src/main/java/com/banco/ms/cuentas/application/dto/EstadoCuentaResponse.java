package com.banco.ms.cuentas.application.dto;

import java.time.LocalDate;
import java.util.List;



public record EstadoCuentaResponse(
        String clienteId,
        String cliente,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        List<CuentaReporteResponse> cuentas) {
}
