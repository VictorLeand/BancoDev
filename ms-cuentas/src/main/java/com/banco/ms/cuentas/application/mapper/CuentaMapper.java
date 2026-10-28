package com.banco.ms.cuentas.application.mapper;

import com.banco.ms.cuentas.application.dto.CuentaResponse;
import com.banco.ms.cuentas.domain.model.Cuenta;

public final class CuentaMapper {

    private CuentaMapper() {
    }

    public static CuentaResponse toResponse(Cuenta cuenta) {
        return new CuentaResponse(
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoInicial(),
                cuenta.getSaldoDisponible(),
                cuenta.isEstado(),
                cuenta.getClienteId());
    }
}