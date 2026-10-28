package com.banco.ms.cuentas.application.usecase;

import com.banco.ms.cuentas.application.dto.MovimientoActualizacionRequest;
import com.banco.ms.cuentas.application.dto.MovimientoRequest;
import com.banco.ms.cuentas.application.dto.MovimientoResponse;

import java.util.List;

public interface MovimientoUseCase {

    MovimientoResponse registrar(MovimientoRequest request);

    MovimientoResponse obtener(Long id);

    List<MovimientoResponse> listar(String numeroCuenta);

    MovimientoResponse actualizar(Long id, MovimientoActualizacionRequest request);
}
