package com.banco.ms.clientes.domain.model;

import com.banco.ms.clientes.domain.exception.ValidacionDominioException;

public abstract class Persona {

    private static final int EDAD_MAXIMA = 150;

    private final Long id;
    private String nombre;
    private Genero genero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;

    protected Persona(Long id, String nombre, Genero genero, Integer edad,
                      String identificacion, String direccion, String telefono) {
        this.id = id;
        actualizarDatosPersonales(nombre, genero, edad, identificacion, direccion, telefono);
    }

    public final void actualizarDatosPersonales(String nombre, Genero genero, Integer edad,
                                                String identificacion, String direccion, String telefono) {
        this.nombre = requerirTexto(nombre, "nombre");
        this.genero = requerirValor(genero, "genero");
        this.edad = validarEdad(edad);
        this.identificacion = requerirTexto(identificacion, "identificacion");
        this.direccion = requerirTexto(direccion, "direccion");
        this.telefono = requerirTexto(telefono, "telefono");
    }

    protected static String requerirTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ValidacionDominioException("El campo '" + campo + "' es obligatorio");
        }
        return valor.trim();
    }

    protected static <T> T requerirValor(T valor, String campo) {
        if (valor == null) {
            throw new ValidacionDominioException("El campo '" + campo + "' es obligatorio");
        }
        return valor;
    }

    private static Integer validarEdad(Integer edad) {
        requerirValor(edad, "edad");
        if (edad < 0 || edad > EDAD_MAXIMA) {
            throw new ValidacionDominioException("La edad debe estar entre 0 y " + EDAD_MAXIMA);
        }
        return edad;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Genero getGenero() { return genero; }

    public Integer getEdad() {
        return edad;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }
}
