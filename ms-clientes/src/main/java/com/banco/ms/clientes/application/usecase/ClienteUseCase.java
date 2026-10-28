package com.banco.ms.clientes.application.usecase;

import com.banco.ms.clientes.application.dto.ClienteActualizacionRequest;
import com.banco.ms.clientes.application.dto.ClienteParcialRequest;
import com.banco.ms.clientes.application.dto.ClienteRequest;
import com.banco.ms.clientes.application.dto.ClienteResponse;

import java.util.List;

public interface ClienteUseCase {

    ClienteResponse crear(ClienteRequest request);

    ClienteResponse obtener(String clienteId);

    List<ClienteResponse> listar();

    ClienteResponse actualizar(String clienteId, ClienteActualizacionRequest request);

    ClienteResponse actualizarParcial(String clienteId, ClienteParcialRequest request);

    void eliminar(String clienteId);
}
