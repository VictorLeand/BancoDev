package com.banco.ms.clientes.domain.model;

public class Cliente extends Persona {

    private final String clienteId;
    private String contrasena;
    private boolean estado;

    private Cliente(Builder builder) {
        super(builder.id, builder.nombre, builder.genero, builder.edad,
                builder.identificacion, builder.direccion, builder.telefono);
        this.clienteId = requerirTexto(builder.clienteId, "clienteId");
        this.contrasena = requerirTexto(builder.contrasena, "contrasena");
        this.estado = builder.estado;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void cambiarContrasena(String contrasenaCifrada) {
        this.contrasena = requerirTexto(contrasenaCifrada, "contrasena");
    }

    public void activar() {
        this.estado = true;
    }

    public void desactivar() {
        this.estado = false;
    }

    public void cambiarEstado(boolean activo) {
        if (activo) {
            activar();
        } else {
            desactivar();
        }
    }

    public boolean estaActivo() {
        return estado;
    }

    public String getClienteId() {
        return clienteId;
    }

    public String getContrasena() {
        return contrasena;
    }

    public boolean isEstado() {
        return estado;
    }

    public static final class Builder {

        private Long id;
        private String nombre;
        private Genero genero;
        private Integer edad;
        private String identificacion;
        private String direccion;
        private String telefono;
        private String clienteId;
        private String contrasena;
        private boolean estado = true;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder genero(Genero genero) {
            this.genero = genero;
            return this;
        }

        public Builder edad(Integer edad) {
            this.edad = edad;
            return this;
        }

        public Builder identificacion(String identificacion) {
            this.identificacion = identificacion;
            return this;
        }

        public Builder direccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public Builder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public Builder clienteId(String clienteId) {
            this.clienteId = clienteId;
            return this;
        }

        public Builder contrasena(String contrasena) {
            this.contrasena = contrasena;
            return this;
        }

        public Builder estado(boolean estado) {
            this.estado = estado;
            return this;
        }

        public Cliente build() {
            return new Cliente(this);
        }
    }
}
