package com.banco.ms.cuentas.application.dto;

import com.banco.ms.cuentas.domain.model.TipoCuenta;

import java.math.BigDecimal;

public record CuentaResponse(
        String numeroCuenta,
        TipoCuenta tipoCuenta,
        BigDecimal saldoInicial,
        BigDecimal saldoDisponible,
        boolean estado,
        String clienteId) {
}