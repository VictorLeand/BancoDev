package com.banco.ms.cuentas.application.usecase;

import com.banco.ms.cuentas.application.dto.CuentaActualizacionRequest;
import com.banco.ms.cuentas.application.dto.CuentaParcialRequest;
import com.banco.ms.cuentas.application.dto.CuentaRequest;
import com.banco.ms.cuentas.application.dto.CuentaResponse;

import java.util.List;

public interface CuentaUseCase {

    CuentaResponse crear(CuentaRequest request);

    CuentaResponse obtener(String numeroCuenta);

    List<CuentaResponse> listar(String clienteId);

    CuentaResponse actualizar(String numeroCuenta, CuentaActualizacionRequest request);

    CuentaResponse actualizarParcial(String numeroCuenta, CuentaParcialRequest request);
}
