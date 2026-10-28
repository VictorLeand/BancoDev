package com.banco.ms.cuentas.application.service;

import com.banco.ms.cuentas.application.dto.CuentaReporteResponse;
import com.banco.ms.cuentas.application.dto.EstadoCuentaResponse;
import com.banco.ms.cuentas.application.mapper.MovimientoMapper;
import com.banco.ms.cuentas.application.usecase.ReporteUseCase;
import com.banco.ms.cuentas.domain.exception.RecursoNoEncontradoException;
import com.banco.ms.cuentas.domain.model.ClienteSnapshot;
import com.banco.ms.cuentas.domain.model.Cuenta;
import com.banco.ms.cuentas.domain.model.Movimiento;
import com.banco.ms.cuentas.domain.model.RangoFechas;
import com.banco.ms.cuentas.domain.port.ClienteSnapshotRepositoryPort;
import com.banco.ms.cuentas.domain.port.CuentaRepositoryPort;
import com.banco.ms.cuentas.domain.port.MovimientoRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@Service
@Transactional(readOnly = true)
public class ReporteService implements ReporteUseCase {

    private final ClienteSnapshotRepositoryPort clienteSnapshotRepository;
    private final CuentaRepositoryPort cuentaRepository;
    private final MovimientoRepositoryPort movimientoRepository;

    public ReporteService(ClienteSnapshotRepositoryPort clienteSnapshotRepository,
                          CuentaRepositoryPort cuentaRepository,
                          MovimientoRepositoryPort movimientoRepository) {
        this.clienteSnapshotRepository = clienteSnapshotRepository;
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    public EstadoCuentaResponse generarEstadoCuenta(String clienteId, String rangoFechas) {
        RangoFechas rango = RangoFechas.desdeTexto(rangoFechas);
        ClienteSnapshot cliente = clienteSnapshotRepository.buscarPorClienteId(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente " + clienteId + " no encontrado"));

        Map<String, List<Movimiento>> movimientosPorCuenta = movimientoRepository
                .buscarPorClienteIdYRango(clienteId, rango.desde(), rango.hastaExclusivo())
                .stream()
                .collect(Collectors.groupingBy(Movimiento::getNumeroCuenta));

        List<CuentaReporteResponse> cuentas = cuentaRepository.buscarPorClienteId(clienteId).stream()
                .map(cuenta -> construirDetalle(cuenta,
                        movimientosPorCuenta.getOrDefault(cuenta.getNumeroCuenta(), List.of())))
                .toList();

        return new EstadoCuentaResponse(cliente.clienteId(), cliente.nombre(), rango.inicio(), rango.fin(), cuentas);
    }

    private CuentaReporteResponse construirDetalle(Cuenta cuenta, List<Movimiento> movimientos) {
        BigDecimal totalCreditos = sumar(movimientos.stream().filter(Movimiento::esCredito).toList());
        BigDecimal totalDebitos = sumar(movimientos.stream().filter(m -> !m.esCredito()).toList()).abs();

        return new CuentaReporteResponse(
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoInicial(),
                cuenta.isEstado(),
                cuenta.getSaldoDisponible(),
                totalCreditos,
                totalDebitos,
                movimientos.stream().map(MovimientoMapper::toReporte).toList());
    }

    private static BigDecimal sumar(List<Movimiento> movimientos) {
        return movimientos.stream()
                .map(Movimiento::getValor)
                .reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
    }
}
