package com.banco.ms.cuentas.application.dto;

import com.banco.ms.cuentas.domain.model.TipoCuenta;


public record CuentaParcialRequest(
        TipoCuenta tipoCuenta,
        Boolean estado) {
}
