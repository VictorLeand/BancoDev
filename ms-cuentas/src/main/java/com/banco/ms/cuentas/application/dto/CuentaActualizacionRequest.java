package com.banco.ms.cuentas.application.dto;

import com.banco.ms.cuentas.domain.model.TipoCuenta;
import jakarta.validation.constraints.NotNull;



public record CuentaActualizacionRequest(
        @NotNull TipoCuenta tipoCuenta,
        @NotNull Boolean estado) {
}
