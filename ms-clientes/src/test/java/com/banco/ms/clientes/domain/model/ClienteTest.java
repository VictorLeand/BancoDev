package com.banco.ms.clientes.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.banco.ms.clientes.domain.model.Genero.MASCULINO;
import static org.assertj.core.api.Assertions.assertThat;

class ClienteTest {

    @Test
    @DisplayName("Crea un cliente válido que hereda los datos de Persona")
    void debeCrearClienteConDatosDePersona_cuandoLosDatosSonValidos() {
        Cliente cliente = Cliente.builder()
                .nombre("  Jose Lema  ")
                .genero(MASCULINO)
                .edad(35)
                .identificacion("1712345678")
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .clienteId("3F9A1C2B7D4E8A61B2C3D4E5F6A7B8C9")
                .contrasena("hash-1234")
                .estado(true)
                .build();

        assertThat(cliente).isInstanceOf(Persona.class);
        assertThat(cliente.getNombre()).isEqualTo("Jose Lema");
        assertThat(cliente.getEdad()).isEqualTo(35);
        assertThat(cliente.getIdentificacion()).isEqualTo("1712345678");
        assertThat(cliente.getClienteId()).isEqualTo("3F9A1C2B7D4E8A61B2C3D4E5F6A7B8C9");
        assertThat(cliente.estaActivo()).isTrue();
    }
}