package com.banco.ms.cuentas.infrastructure.persistence.repository;

import com.banco.ms.cuentas.infrastructure.persistence.entity.CuentaEntity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CuentaJpaRepository extends JpaRepository<CuentaEntity, Long> {

    Optional<CuentaEntity> findByNumeroCuenta(String numeroCuenta);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "5000"))
    @Query("select c from CuentaEntity c where c.numeroCuenta = :numeroCuenta")
    Optional<CuentaEntity> findByNumeroCuentaParaActualizar(@Param("numeroCuenta") String numeroCuenta);

    List<CuentaEntity> findByClienteIdOrderByNumeroCuentaAsc(String clienteId);

    boolean existsByNumeroCuenta(String numeroCuenta);
}
