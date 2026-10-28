package com.banco.ms.cuentas.infrastructure.persistence.adapter;

import com.banco.ms.cuentas.domain.model.Cuenta;
import com.banco.ms.cuentas.domain.port.CuentaRepositoryPort;
import com.banco.ms.cuentas.infrastructure.persistence.mapper.PersistenciaMapper;
import com.banco.ms.cuentas.infrastructure.persistence.repository.CuentaJpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CuentaPersistenceAdapter implements CuentaRepositoryPort {

    private final CuentaJpaRepository jpaRepository;

    public CuentaPersistenceAdapter(CuentaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cuenta guardar(Cuenta cuenta) {
        return PersistenciaMapper.toDomain(jpaRepository.save(PersistenciaMapper.toEntity(cuenta)));
    }

    @Override
    public Optional<Cuenta> buscarPorNumeroCuenta(String numeroCuenta) {
        return jpaRepository.findByNumeroCuenta(numeroCuenta).map(PersistenciaMapper::toDomain);
    }

    @Override
    public Optional<Cuenta> buscarPorNumeroCuentaConBloqueo(String numeroCuenta) {
        return jpaRepository.findByNumeroCuentaParaActualizar(numeroCuenta).map(PersistenciaMapper::toDomain);
    }

    @Override
    public List<Cuenta> buscarTodas() {
        return jpaRepository.findAll(Sort.by("numeroCuenta")).stream()
                .map(PersistenciaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Cuenta> buscarPorClienteId(String clienteId) {
        return jpaRepository.findByClienteIdOrderByNumeroCuentaAsc(clienteId).stream()
                .map(PersistenciaMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorNumeroCuenta(String numeroCuenta) {
        return jpaRepository.existsByNumeroCuenta(numeroCuenta);
    }
}
