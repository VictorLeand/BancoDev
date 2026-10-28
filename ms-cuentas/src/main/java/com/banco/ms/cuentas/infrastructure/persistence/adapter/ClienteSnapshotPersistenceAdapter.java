package com.banco.ms.cuentas.infrastructure.persistence.adapter;

import com.banco.ms.cuentas.domain.model.ClienteSnapshot;
import com.banco.ms.cuentas.domain.port.ClienteSnapshotRepositoryPort;
import com.banco.ms.cuentas.infrastructure.persistence.mapper.PersistenciaMapper;
import com.banco.ms.cuentas.infrastructure.persistence.repository.ClienteSnapshotJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClienteSnapshotPersistenceAdapter implements ClienteSnapshotRepositoryPort {

    private final ClienteSnapshotJpaRepository jpaRepository;

    public ClienteSnapshotPersistenceAdapter(ClienteSnapshotJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ClienteSnapshot guardar(ClienteSnapshot snapshot) {
        return PersistenciaMapper.toDomain(jpaRepository.save(PersistenciaMapper.toEntity(snapshot)));
    }

    @Override
    public Optional<ClienteSnapshot> buscarPorClienteId(String clienteId) {
        return jpaRepository.findById(clienteId).map(PersistenciaMapper::toDomain);
    }
}
