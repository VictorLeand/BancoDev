package com.banco.ms.cuentas.application.dto;

import com.banco.ms.cuentas.domain.model.TipoCuenta;

import java.math.BigDecimal;
import java.util.List;

public record CuentaReporteResponse(
        String numeroCuenta,
        TipoCuenta tipoCuenta,
        BigDecimal saldoInicial,
        boolean estado,
        BigDecimal saldoDisponible,
        BigDecimal totalCreditos,
        BigDecimal totalDebitos,
        List<MovimientoReporteResponse> movimientos) {
}
