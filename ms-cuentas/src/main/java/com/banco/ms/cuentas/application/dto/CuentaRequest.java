package com.banco.ms.cuentas.application.dto;

import com.banco.ms.cuentas.domain.model.TipoCuenta;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CuentaRequest(
        @NotBlank @Pattern(regexp = "^[0-9]{6,20}$", message = "debe contener entre 6 y 20 dígitos")
        String numeroCuenta,
        @NotNull TipoCuenta tipoCuenta,
        @NotNull @DecimalMin("0.00") @Digits(integer = 13, fraction = 2) BigDecimal saldoInicial,
        @NotNull Boolean estado,
        @NotBlank @Size(max = 36) String clienteId) {
}
