package com.banco.ms.cuentas.infrastructure.persistence.repository;

import com.banco.ms.cuentas.infrastructure.persistence.entity.ClienteSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteSnapshotJpaRepository extends JpaRepository<ClienteSnapshotEntity, String> {
}
