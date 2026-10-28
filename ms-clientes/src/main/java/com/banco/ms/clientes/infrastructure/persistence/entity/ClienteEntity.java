package com.banco.ms.clientes.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "persona_id")
@Getter
@Setter
@NoArgsConstructor
public class ClienteEntity extends PersonaEntity {

    @Column(name = "cliente_id", nullable = false, unique = true, length = 36)
    private String clienteId;

    @Column(nullable = false, length = 100)
    private String contrasena;

    @Column(nullable = false)
    private Boolean estado;
}
