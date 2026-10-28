package com.banco.ms.clientes.infrastructure.persistence.adapter;

import com.banco.ms.clientes.domain.model.Cliente;
import com.banco.ms.clientes.domain.port.ClienteRepositoryPort;
import com.banco.ms.clientes.infrastructure.persistence.mapper.ClienteEntityMapper;
import com.banco.ms.clientes.infrastructure.persistence.repository.ClienteJpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ClientePersistenceAdapter implements ClienteRepositoryPort {

    private final ClienteJpaRepository jpaRepository;

    public ClientePersistenceAdapter(ClienteJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        return ClienteEntityMapper.toDomain(jpaRepository.save(ClienteEntityMapper.toEntity(cliente)));
    }

    @Override
    public Optional<Cliente> buscarPorClienteId(String clienteId) {
        return jpaRepository.findByClienteId(clienteId).map(ClienteEntityMapper::toDomain);
    }

    @Override
    public List<Cliente> buscarTodos() {
        return jpaRepository.findAll(Sort.by("clienteId")).stream()
                .map(ClienteEntityMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorIdentificacion(String identificacion) {
        return jpaRepository.existsByIdentificacion(identificacion);
    }

    @Override
    public boolean existePorIdentificacionEnOtroCliente(String identificacion, String clienteId) {
        return jpaRepository.existsByIdentificacionAndClienteIdNot(identificacion, clienteId);
    }

    @Override
    public void eliminar(Cliente cliente) {
        jpaRepository.findByClienteId(cliente.getClienteId()).ifPresent(jpaRepository::delete);
    }
}
