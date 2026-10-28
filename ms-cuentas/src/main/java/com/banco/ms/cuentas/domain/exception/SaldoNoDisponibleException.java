package com.banco.ms.cuentas.domain.exception;


public class SaldoNoDisponibleException extends ReglaNegocioException {

    public static final String MENSAJE = "Saldo no disponible";

    public SaldoNoDisponibleException() {
        super(MENSAJE);
    }
}
