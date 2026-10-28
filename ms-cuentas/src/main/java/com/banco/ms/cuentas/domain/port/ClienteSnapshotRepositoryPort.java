package com.banco.ms.cuentas.domain.port;

import com.banco.ms.cuentas.domain.model.ClienteSnapshot;

import java.util.Optional;

public interface ClienteSnapshotRepositoryPort {

    ClienteSnapshot guardar(ClienteSnapshot snapshot);

    Optional<ClienteSnapshot> buscarPorClienteId(String clienteId);
}
