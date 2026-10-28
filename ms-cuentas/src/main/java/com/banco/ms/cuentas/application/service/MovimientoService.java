package com.banco.ms.cuentas.application.service;

import com.banco.ms.cuentas.application.dto.MovimientoActualizacionRequest;
import com.banco.ms.cuentas.application.dto.MovimientoRequest;
import com.banco.ms.cuentas.application.dto.MovimientoResponse;
import com.banco.ms.cuentas.application.mapper.MovimientoMapper;
import com.banco.ms.cuentas.application.usecase.MovimientoUseCase;
import com.banco.ms.cuentas.domain.exception.RecursoNoEncontradoException;
import com.banco.ms.cuentas.domain.exception.ReglaNegocioException;
import com.banco.ms.cuentas.domain.model.Cuenta;
import com.banco.ms.cuentas.domain.model.Movimiento;
import com.banco.ms.cuentas.domain.port.CuentaRepositoryPort;
import com.banco.ms.cuentas.domain.port.MovimientoRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;



@Service
@Transactional
public class MovimientoService implements MovimientoUseCase {

    private final CuentaRepositoryPort cuentaRepository;
    private final MovimientoRepositoryPort movimientoRepository;
    private final Clock clock;

    public MovimientoService(CuentaRepositoryPort cuentaRepository,
                             MovimientoRepositoryPort movimientoRepository,
                             Clock clock) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
        this.clock = clock;
    }

    @Override
    public MovimientoResponse registrar(MovimientoRequest request) {
        Cuenta cuenta = bloquearCuenta(request.numeroCuenta());
        Movimiento movimiento = cuenta.registrarMovimiento(request.valor(), LocalDateTime.now(clock));

        cuentaRepository.guardar(cuenta);
        return MovimientoMapper.toResponse(movimientoRepository.guardar(movimiento));
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoResponse obtener(Long id) {
        return MovimientoMapper.toResponse(buscarMovimiento(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoResponse> listar(String numeroCuenta) {
        List<Movimiento> movimientos = numeroCuenta == null || numeroCuenta.isBlank()
                ? movimientoRepository.buscarTodos()
                : movimientoRepository.buscarPorNumeroCuenta(numeroCuenta);
        return movimientos.stream().map(MovimientoMapper::toResponse).toList();
    }

    @Override
    public MovimientoResponse actualizar(Long id, MovimientoActualizacionRequest request) {
        Movimiento movimiento = buscarMovimiento(id);
        Cuenta cuenta = bloquearCuenta(movimiento.getNumeroCuenta());

        boolean esUltimo = movimientoRepository.buscarUltimoPorNumeroCuenta(cuenta.getNumeroCuenta())
                .map(ultimo -> ultimo.getId().equals(movimiento.getId()))
                .orElse(false);
        if (!esUltimo) {
            throw new ReglaNegocioException("Solo se puede modificar el último movimiento de la cuenta "
                    + cuenta.getNumeroCuenta());
        }

        cuenta.corregirMovimiento(movimiento, request.valor());
        cuentaRepository.guardar(cuenta);
        return MovimientoMapper.toResponse(movimientoRepository.guardar(movimiento));
    }

    private Cuenta bloquearCuenta(String numeroCuenta) {
        return cuentaRepository.buscarPorNumeroCuentaConBloqueo(numeroCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta " + numeroCuenta + " no encontrada"));
    }

    private Movimiento buscarMovimiento(Long id) {
        return movimientoRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Movimiento " + id + " no encontrado"));
    }
}
