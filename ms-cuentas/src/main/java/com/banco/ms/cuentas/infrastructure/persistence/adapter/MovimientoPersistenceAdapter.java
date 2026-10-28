package com.banco.ms.cuentas.infrastructure.persistence.adapter;

import com.banco.ms.cuentas.domain.exception.RecursoNoEncontradoException;
import com.banco.ms.cuentas.domain.model.Movimiento;
import com.banco.ms.cuentas.domain.port.MovimientoRepositoryPort;
import com.banco.ms.cuentas.infrastructure.persistence.entity.CuentaEntity;
import com.banco.ms.cuentas.infrastructure.persistence.mapper.PersistenciaMapper;
import com.banco.ms.cuentas.infrastructure.persistence.repository.CuentaJpaRepository;
import com.banco.ms.cuentas.infrastructure.persistence.repository.MovimientoJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class MovimientoPersistenceAdapter implements MovimientoRepositoryPort {

    private final MovimientoJpaRepository movimientoJpaRepository;
    private final CuentaJpaRepository cuentaJpaRepository;

    public MovimientoPersistenceAdapter(MovimientoJpaRepository movimientoJpaRepository,
                                        CuentaJpaRepository cuentaJpaRepository) {
        this.movimientoJpaRepository = movimientoJpaRepository;
        this.cuentaJpaRepository = cuentaJpaRepository;
    }

    @Override
    public Movimiento guardar(Movimiento movimiento) {
        CuentaEntity cuenta = cuentaJpaRepository.findByNumeroCuenta(movimiento.getNumeroCuenta())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cuenta " + movimiento.getNumeroCuenta() + " no encontrada"));
        return PersistenciaMapper.toDomain(
                movimientoJpaRepository.save(PersistenciaMapper.toEntity(movimiento, cuenta)));
    }

    @Override
    public Optional<Movimiento> buscarPorId(Long id) {
        return movimientoJpaRepository.findByIdConCuenta(id).map(PersistenciaMapper::toDomain);
    }

    @Override
    public List<Movimiento> buscarTodos() {
        return movimientoJpaRepository.findAllConCuenta().stream()
                .map(PersistenciaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Movimiento> buscarPorNumeroCuenta(String numeroCuenta) {
        return movimientoJpaRepository.findByNumeroCuentaConCuenta(numeroCuenta).stream()
                .map(PersistenciaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Movimiento> buscarUltimoPorNumeroCuenta(String numeroCuenta) {
        return movimientoJpaRepository.findFirstByCuentaNumeroCuentaOrderByFechaDescIdDesc(numeroCuenta)
                .map(PersistenciaMapper::toDomain);
    }

    @Override
    public List<Movimiento> buscarPorClienteIdYRango(String clienteId, LocalDateTime desde,
                                                     LocalDateTime hastaExclusivo) {
        return movimientoJpaRepository.findByClienteIdAndRango(clienteId, desde, hastaExclusivo).stream()
                .map(PersistenciaMapper::toDomain)
                .toList();
    }
}
