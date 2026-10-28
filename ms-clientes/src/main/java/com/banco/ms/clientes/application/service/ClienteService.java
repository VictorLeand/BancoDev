package com.banco.ms.clientes.application.service;

import com.banco.ms.clientes.application.dto.ClienteActualizacionRequest;
import com.banco.ms.clientes.application.dto.ClienteParcialRequest;
import com.banco.ms.clientes.application.dto.ClienteRequest;
import com.banco.ms.clientes.application.dto.ClienteResponse;
import com.banco.ms.clientes.application.mapper.ClienteMapper;
import com.banco.ms.clientes.application.usecase.ClienteUseCase;
import com.banco.ms.clientes.domain.event.ClienteEvento;
import com.banco.ms.clientes.domain.exception.RecursoDuplicadoException;
import com.banco.ms.clientes.domain.exception.RecursoNoEncontradoException;
import com.banco.ms.clientes.domain.model.Cliente;
import com.banco.ms.clientes.domain.port.CifradorContrasenaPort;
import com.banco.ms.clientes.domain.port.ClienteEventoPublisherPort;
import com.banco.ms.clientes.domain.port.ClienteRepositoryPort;
import com.banco.ms.clientes.domain.port.GeneradorClienteIdPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.requireNonNullElse;

@Service
@Transactional
public class ClienteService implements ClienteUseCase {

    private final ClienteRepositoryPort clienteRepository;
    private final ClienteEventoPublisherPort eventoPublisher;
    private final CifradorContrasenaPort cifradorContrasena;
    private final GeneradorClienteIdPort generadorClienteId;

    public ClienteService(ClienteRepositoryPort clienteRepository,
                          ClienteEventoPublisherPort eventoPublisher,
                          CifradorContrasenaPort cifradorContrasena, GeneradorClienteIdPort generadorClienteId) {
        this.clienteRepository = clienteRepository;
        this.eventoPublisher = eventoPublisher;
        this.cifradorContrasena = cifradorContrasena;
        this.generadorClienteId = generadorClienteId;
    }

    @Override
    public ClienteResponse crear(ClienteRequest request) {
        if (clienteRepository.existePorIdentificacion(request.identificacion())) {
            throw new RecursoDuplicadoException("Ya existe un cliente con identificación " + request.identificacion());
        }

        Cliente cliente = Cliente.builder()
                .nombre(request.nombre())
                .genero(request.genero())
                .edad(request.edad())
                .identificacion(request.identificacion())
                .direccion(request.direccion())
                .telefono(request.telefono())
                .clienteId(generadorClienteId.generar())
                .contrasena(cifradorContrasena.cifrar(request.contrasena()))
                .estado(request.estado())
                .build();

        Cliente guardado = clienteRepository.guardar(cliente);
        eventoPublisher.publicar(ClienteEvento.creado(guardado));
        return ClienteMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse obtener(String clienteId) {
        return ClienteMapper.toResponse(buscarCliente(clienteId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.buscarTodos().stream()
                .map(ClienteMapper::toResponse)
                .toList();
    }

    @Override
    public ClienteResponse actualizar(String clienteId, ClienteActualizacionRequest request) {
        Cliente cliente = buscarCliente(clienteId);
        validarIdentificacionDisponible(request.identificacion(), clienteId);

        cliente.actualizarDatosPersonales(request.nombre(), request.genero(), request.edad(),
                request.identificacion(), request.direccion(), request.telefono());
        cliente.cambiarContrasena(cifradorContrasena.cifrar(request.contrasena()));
        cliente.cambiarEstado(request.estado());

        return guardarYNotificarActualizacion(cliente);
    }

    @Override
    public ClienteResponse actualizarParcial(String clienteId, ClienteParcialRequest request) {
        Cliente cliente = buscarCliente(clienteId);
        if (request.identificacion() != null) {
            validarIdentificacionDisponible(request.identificacion(), clienteId);
        }

        cliente.actualizarDatosPersonales(
                requireNonNullElse(request.nombre(), cliente.getNombre()),
                requireNonNullElse(request.genero(), cliente.getGenero()),
                requireNonNullElse(request.edad(), cliente.getEdad()),
                requireNonNullElse(request.identificacion(), cliente.getIdentificacion()),
                requireNonNullElse(request.direccion(), cliente.getDireccion()),
                requireNonNullElse(request.telefono(), cliente.getTelefono()));
        if (request.contrasena() != null) {
            cliente.cambiarContrasena(cifradorContrasena.cifrar(request.contrasena()));
        }
        if (request.estado() != null) {
            cliente.cambiarEstado(request.estado());
        }

        return guardarYNotificarActualizacion(cliente);
    }

    @Override
    public void eliminar(String clienteId) {
        Cliente cliente = buscarCliente(clienteId);
        clienteRepository.eliminar(cliente);
        eventoPublisher.publicar(ClienteEvento.eliminado(cliente));
    }

    private ClienteResponse guardarYNotificarActualizacion(Cliente cliente) {
        Cliente guardado = clienteRepository.guardar(cliente);
        eventoPublisher.publicar(ClienteEvento.actualizado(guardado));
        return ClienteMapper.toResponse(guardado);
    }

    private Cliente buscarCliente(String clienteId) {
        return clienteRepository.buscarPorClienteId(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente " + clienteId + " no encontrado"));
    }

    private void validarIdentificacionDisponible(String identificacion, String clienteId) {
        if (clienteRepository.existePorIdentificacionEnOtroCliente(identificacion, clienteId)) {
            throw new RecursoDuplicadoException("Ya existe otro cliente con identificación " + identificacion);
        }
    }
}