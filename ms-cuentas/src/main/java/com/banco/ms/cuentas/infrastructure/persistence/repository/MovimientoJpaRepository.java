package com.banco.ms.cuentas.infrastructure.persistence.repository;

import com.banco.ms.cuentas.infrastructure.persistence.entity.MovimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovimientoJpaRepository extends JpaRepository<MovimientoEntity, Long> {

    @Query("select m from MovimientoEntity m join fetch m.cuenta where m.id = :id")
    Optional<MovimientoEntity> findByIdConCuenta(@Param("id") Long id);

    @Query("select m from MovimientoEntity m join fetch m.cuenta order by m.fecha desc, m.id desc")
    List<MovimientoEntity> findAllConCuenta();

    @Query("""
            select m from MovimientoEntity m join fetch m.cuenta c
            where c.numeroCuenta = :numeroCuenta
            order by m.fecha desc, m.id desc
            """)
    List<MovimientoEntity> findByNumeroCuentaConCuenta(@Param("numeroCuenta") String numeroCuenta);

    Optional<MovimientoEntity> findFirstByCuentaNumeroCuentaOrderByFechaDescIdDesc(String numeroCuenta);

    @Query("""
            select m from MovimientoEntity m join fetch m.cuenta c
            where c.clienteId = :clienteId
              and m.fecha >= :desde
              and m.fecha < :hasta
            order by c.numeroCuenta, m.fecha, m.id
            """)
    List<MovimientoEntity> findByClienteIdAndRango(@Param("clienteId") String clienteId,
                                                   @Param("desde") LocalDateTime desde,
                                                   @Param("hasta") LocalDateTime hasta);
}
