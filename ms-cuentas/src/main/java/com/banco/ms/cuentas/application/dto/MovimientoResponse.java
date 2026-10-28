package com.banco.ms.cuentas.application.dto;

import com.banco.ms.cuentas.domain.model.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimientoResponse(
        Long id,
        LocalDateTime fecha,
        TipoMovimiento tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldo,
        String numeroCuenta) {
}
