package com.banco.ms.clientes.infrastructure.persistence.repository;

import com.banco.ms.clientes.infrastructure.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByClienteId(String clienteId);

    boolean existsByIdentificacion(String identificacion);

    boolean existsByIdentificacionAndClienteIdNot(String identificacion, String clienteId);
}
