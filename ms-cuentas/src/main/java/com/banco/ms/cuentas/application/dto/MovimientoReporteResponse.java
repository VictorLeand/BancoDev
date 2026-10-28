package com.banco.ms.cuentas.application.dto;

import com.banco.ms.cuentas.domain.model.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimientoReporteResponse(
        LocalDateTime fecha,
        TipoMovimiento tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldoDisponible) {
}
