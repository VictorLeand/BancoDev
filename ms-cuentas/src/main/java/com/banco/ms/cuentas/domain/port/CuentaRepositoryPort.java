package com.banco.ms.cuentas.domain.port;

import com.banco.ms.cuentas.domain.model.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaRepositoryPort {

    Cuenta guardar(Cuenta cuenta);

    Optional<Cuenta> buscarPorNumeroCuenta(String numeroCuenta);


    Optional<Cuenta> buscarPorNumeroCuentaConBloqueo(String numeroCuenta);

    List<Cuenta> buscarTodas();

    List<Cuenta> buscarPorClienteId(String clienteId);

    boolean existePorNumeroCuenta(String numeroCuenta);
}
