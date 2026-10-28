package com.banco.ms.clientes.infrastructure.persistence.mapper;

import com.banco.ms.clientes.domain.model.Cliente;
import com.banco.ms.clientes.infrastructure.persistence.entity.ClienteEntity;

public final class ClienteEntityMapper {

    private ClienteEntityMapper() {
    }

    public static ClienteEntity toEntity(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity();
        entity.setId(cliente.getId());
        entity.setNombre(cliente.getNombre());
        entity.setGenero(cliente.getGenero());
        entity.setEdad(cliente.getEdad());
        entity.setIdentificacion(cliente.getIdentificacion());
        entity.setDireccion(cliente.getDireccion());
        entity.setTelefono(cliente.getTelefono());
        entity.setClienteId(cliente.getClienteId());
        entity.setContrasena(cliente.getContrasena());
        entity.setEstado(cliente.isEstado());
        return entity;
    }

    public static Cliente toDomain(ClienteEntity entity) {
        return Cliente.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .genero(entity.getGenero())
                .edad(entity.getEdad())
                .identificacion(entity.getIdentificacion())
                .direccion(entity.getDireccion())
                .telefono(entity.getTelefono())
                .clienteId(entity.getClienteId())
                .contrasena(entity.getContrasena())
                .estado(entity.getEstado())
                .build();
    }
}
