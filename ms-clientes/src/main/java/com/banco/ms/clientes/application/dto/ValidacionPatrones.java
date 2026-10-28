package com.banco.ms.clientes.application.dto;

final class ValidacionPatrones {

    static final String TELEFONO = "^[0-9+\\-\\s]{7,20}$";
    static final String TELEFONO_MENSAJE = "debe contener entre 7 y 20 dígitos, espacios, '+' o '-'";

    private ValidacionPatrones() {
    }
}
