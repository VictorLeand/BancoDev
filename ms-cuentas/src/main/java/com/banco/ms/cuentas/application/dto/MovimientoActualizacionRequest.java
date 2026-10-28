package com.banco.ms.cuentas.application.dto;



import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MovimientoActualizacionRequest(
        @NotNull @Digits(integer = 13, fraction = 2) BigDecimal valor) {
}
