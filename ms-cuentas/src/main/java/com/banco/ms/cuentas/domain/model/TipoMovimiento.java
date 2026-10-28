package com.banco.ms.cuentas.domain.model;

import com.banco.ms.cuentas.domain.exception.ValidacionDominioException;

import java.math.BigDecimal;

public enum TipoMovimiento {
    DEPOSITO,
    RETIRO;



    public static TipoMovimiento desde(BigDecimal valor) {
        if (valor == null || valor.signum() == 0) {
            throw new ValidacionDominioException("El valor del movimiento debe ser distinto de cero");
        }
        return valor.signum() > 0 ? DEPOSITO : RETIRO;
    }
}
