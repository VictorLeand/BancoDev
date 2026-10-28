package com.banco.ms.cuentas.application.mapper;

import com.banco.ms.cuentas.application.dto.MovimientoReporteResponse;
import com.banco.ms.cuentas.application.dto.MovimientoResponse;
import com.banco.ms.cuentas.domain.model.Movimiento;

public final class MovimientoMapper {

    private MovimientoMapper() {
    }

    public static MovimientoResponse toResponse(Movimiento movimiento) {
        return new MovimientoResponse(
                movimiento.getId(),
                movimiento.getFecha(),
                movimiento.getTipoMovimiento(),
                movimiento.getValor(),
                movimiento.getSaldo(),
                movimiento.getNumeroCuenta());
    }

    public static MovimientoReporteResponse toReporte(Movimiento movimiento) {
        return new MovimientoReporteResponse(
                movimiento.getFecha(),
                movimiento.getTipoMovimiento(),
                movimiento.getValor(),
                movimiento.getSaldo());
    }
}
