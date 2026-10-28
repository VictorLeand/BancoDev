package com.banco.ms.clientes.domain.port;

import com.banco.ms.clientes.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {

    Cliente guardar(Cliente cliente);

    Optional<Cliente> buscarPorClienteId(String clienteId);

    List<Cliente> buscarTodos();

    boolean existePorIdentificacion(String identificacion);

    boolean existePorIdentificacionEnOtroCliente(String identificacion, String clienteId);

    void eliminar(Cliente cliente);
}