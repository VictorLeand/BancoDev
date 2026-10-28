package com.banco.ms.cuentas.application.usecase;

import com.banco.ms.cuentas.application.dto.EstadoCuentaResponse;

public interface ReporteUseCase {

    EstadoCuentaResponse generarEstadoCuenta(String clienteId, String rangoFechas);
}
