package com.banco.ms.cuentas.domain.model;

import com.banco.ms.cuentas.domain.exception.ReglaNegocioException;
import com.banco.ms.cuentas.domain.exception.SaldoNoDisponibleException;
import com.banco.ms.cuentas.domain.exception.ValidacionDominioException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;


public class Cuenta {

    private static final int ESCALA_MONETARIA = 2;

    private final Long id;
    private final String numeroCuenta;
    private TipoCuenta tipoCuenta;
    private final BigDecimal saldoInicial;
    private BigDecimal saldoDisponible;
    private boolean estado;
    private final String clienteId;

    private Cuenta(Long id, String numeroCuenta, TipoCuenta tipoCuenta, BigDecimal saldoInicial,
                   BigDecimal saldoDisponible, boolean estado, String clienteId) {
        this.id = id;
        this.numeroCuenta = requerirTexto(numeroCuenta, "numeroCuenta");
        this.tipoCuenta = requerirValor(tipoCuenta, "tipoCuenta");
        this.saldoInicial = validarSaldo(saldoInicial, "saldoInicial");
        this.saldoDisponible = validarSaldo(saldoDisponible, "saldoDisponible");
        this.estado = estado;
        this.clienteId = requerirTexto(clienteId, "clienteId");
    }

    public static Cuenta abrir(String numeroCuenta, TipoCuenta tipoCuenta, BigDecimal saldoInicial,
                               boolean estado, String clienteId) {
        return new Cuenta(null, numeroCuenta, tipoCuenta, saldoInicial, saldoInicial, estado, clienteId);
    }

    public static Cuenta reconstruir(Long id, String numeroCuenta, TipoCuenta tipoCuenta, BigDecimal saldoInicial,
                                     BigDecimal saldoDisponible, boolean estado, String clienteId) {
        return new Cuenta(id, numeroCuenta, tipoCuenta, saldoInicial, saldoDisponible, estado, clienteId);
    }

    public Movimiento registrarMovimiento(BigDecimal valor, LocalDateTime fecha) {
        validarCuentaActiva();
        BigDecimal valorNormalizado = normalizarMonto(valor, "valor");
        TipoMovimiento tipo = TipoMovimiento.desde(valorNormalizado);
        BigDecimal nuevoSaldo = calcularNuevoSaldo(saldoDisponible, valorNormalizado);

        this.saldoDisponible = nuevoSaldo;
        return Movimiento.nuevo(fecha, tipo, valorNormalizado, nuevoSaldo, numeroCuenta);
    }


    public void corregirMovimiento(Movimiento movimiento, BigDecimal nuevoValor) {
        validarCuentaActiva();
        if (!numeroCuenta.equals(movimiento.getNumeroCuenta())) {
            throw new ReglaNegocioException("El movimiento no pertenece a la cuenta " + numeroCuenta);
        }
        BigDecimal valorNormalizado = normalizarMonto(nuevoValor, "valor");
        TipoMovimiento tipo = TipoMovimiento.desde(valorNormalizado);
        BigDecimal saldoAntesDelMovimiento = saldoDisponible.subtract(movimiento.getValor());
        BigDecimal nuevoSaldo = calcularNuevoSaldo(saldoAntesDelMovimiento, valorNormalizado);

        this.saldoDisponible = nuevoSaldo;
        movimiento.corregir(tipo, valorNormalizado, nuevoSaldo);
    }

    public void actualizar(TipoCuenta nuevoTipo, boolean activa) {
        this.tipoCuenta = requerirValor(nuevoTipo, "tipoCuenta");
        this.estado = activa;
    }

    public void desactivar() {
        this.estado = false;
    }

    public boolean estaActiva() {
        return estado;
    }

    private void validarCuentaActiva() {
        if (!estado) {
            throw new ReglaNegocioException("La cuenta " + numeroCuenta + " está inactiva");
        }
    }

    private static BigDecimal calcularNuevoSaldo(BigDecimal saldoBase, BigDecimal valor) {
        BigDecimal resultado = saldoBase.add(valor);
        if (resultado.signum() < 0) {
            throw new SaldoNoDisponibleException();
        }
        return resultado;
    }

    private static BigDecimal validarSaldo(BigDecimal saldo, String campo) {
        BigDecimal normalizado = normalizarMonto(saldo, campo);
        if (normalizado.signum() < 0) {
            throw new ValidacionDominioException("El campo '" + campo + "' no puede ser negativo");
        }
        return normalizado;
    }

    private static BigDecimal normalizarMonto(BigDecimal monto, String campo) {
        requerirValor(monto, campo);
        if (monto.stripTrailingZeros().scale() > ESCALA_MONETARIA) {
            throw new ValidacionDominioException("El campo '" + campo + "' admite máximo "
                    + ESCALA_MONETARIA + " decimales");
        }
        return monto.setScale(ESCALA_MONETARIA, RoundingMode.UNNECESSARY);
    }

    private static String requerirTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ValidacionDominioException("El campo '" + campo + "' es obligatorio");
        }
        return valor.trim();
    }

    private static <T> T requerirValor(T valor, String campo) {
        if (valor == null) {
            throw new ValidacionDominioException("El campo '" + campo + "' es obligatorio");
        }
        return valor;
    }

    public Long getId() {
        return id;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public boolean isEstado() {
        return estado;
    }

    public String getClienteId() {
        return clienteId;
    }
}