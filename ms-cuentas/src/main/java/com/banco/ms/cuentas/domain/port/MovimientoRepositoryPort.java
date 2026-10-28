package com.banco.ms.cuentas.domain.port;

import com.banco.ms.cuentas.domain.model.Movimiento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovimientoRepositoryPort {

    Movimiento guardar(Movimiento movimiento);

    Optional<Movimiento> buscarPorId(Long id);

    List<Movimiento> buscarTodos();

    List<Movimiento> buscarPorNumeroCuenta(String numeroCuenta);

    Optional<Movimiento> buscarUltimoPorNumeroCuenta(String numeroCuenta);

    List<Movimiento> buscarPorClienteIdYRango(String clienteId, LocalDateTime desde, LocalDateTime hastaExclusivo);
}