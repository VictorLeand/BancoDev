package com.banco.ms.cuentas.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Movimiento {

    private final Long id;
    private final LocalDateTime fecha;
    private TipoMovimiento tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private final String numeroCuenta;

    private Movimiento(Long id, LocalDateTime fecha, TipoMovimiento tipoMovimiento, BigDecimal valor,
                       BigDecimal saldo, String numeroCuenta) {
        this.id = id;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
        this.numeroCuenta = numeroCuenta;
    }

    static Movimiento nuevo(LocalDateTime fecha, TipoMovimiento tipoMovimiento, BigDecimal valor,
                            BigDecimal saldo, String numeroCuenta) {
        return new Movimiento(null, fecha, tipoMovimiento, valor, saldo, numeroCuenta);
    }

    public static Movimiento reconstruir(Long id, LocalDateTime fecha, TipoMovimiento tipoMovimiento,
                                         BigDecimal valor, BigDecimal saldo, String numeroCuenta) {
        return new Movimiento(id, fecha, tipoMovimiento, valor, saldo, numeroCuenta);
    }

    void corregir(TipoMovimiento nuevoTipo, BigDecimal nuevoValor, BigDecimal nuevoSaldo) {
        this.tipoMovimiento = nuevoTipo;
        this.valor = nuevoValor;
        this.saldo = nuevoSaldo;
    }

    public boolean esCredito() {
        return tipoMovimiento == TipoMovimiento.DEPOSITO;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }
}