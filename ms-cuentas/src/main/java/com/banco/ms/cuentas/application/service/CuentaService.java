package com.banco.ms.cuentas.application.service;

import com.banco.ms.cuentas.application.dto.CuentaActualizacionRequest;
import com.banco.ms.cuentas.application.dto.CuentaParcialRequest;
import com.banco.ms.cuentas.application.dto.CuentaRequest;
import com.banco.ms.cuentas.application.dto.CuentaResponse;
import com.banco.ms.cuentas.application.mapper.CuentaMapper;
import com.banco.ms.cuentas.application.usecase.CuentaUseCase;
import com.banco.ms.cuentas.domain.exception.RecursoDuplicadoException;
import com.banco.ms.cuentas.domain.exception.RecursoNoEncontradoException;
import com.banco.ms.cuentas.domain.exception.ReglaNegocioException;
import com.banco.ms.cuentas.domain.model.ClienteSnapshot;
import com.banco.ms.cuentas.domain.model.Cuenta;
import com.banco.ms.cuentas.domain.port.ClienteSnapshotRepositoryPort;
import com.banco.ms.cuentas.domain.port.CuentaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.requireNonNullElse;

@Service
@Transactional
public class CuentaService implements CuentaUseCase {

    private final CuentaRepositoryPort cuentaRepository;
    private final ClienteSnapshotRepositoryPort clienteSnapshotRepository;

    public CuentaService(CuentaRepositoryPort cuentaRepository,
                         ClienteSnapshotRepositoryPort clienteSnapshotRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteSnapshotRepository = clienteSnapshotRepository;
    }

    @Override
    public CuentaResponse crear(CuentaRequest request) {
        if (cuentaRepository.existePorNumeroCuenta(request.numeroCuenta())) {
            throw new RecursoDuplicadoException("Ya existe la cuenta " + request.numeroCuenta());
        }
        ClienteSnapshot cliente = clienteSnapshotRepository.buscarPorClienteId(request.clienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente " + request.clienteId() + " no encontrado"));
        if (!cliente.estado()) {
            throw new ReglaNegocioException("El cliente " + request.clienteId() + " está inactivo");
        }

        Cuenta cuenta = Cuenta.abrir(request.numeroCuenta(), request.tipoCuenta(), request.saldoInicial(),
                request.estado(), request.clienteId());
        return CuentaMapper.toResponse(cuentaRepository.guardar(cuenta));
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaResponse obtener(String numeroCuenta) {
        return CuentaMapper.toResponse(buscarCuenta(numeroCuenta));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaResponse> listar(String clienteId) {
        List<Cuenta> cuentas = clienteId == null || clienteId.isBlank()
                ? cuentaRepository.buscarTodas()
                : cuentaRepository.buscarPorClienteId(clienteId);
        return cuentas.stream().map(CuentaMapper::toResponse).toList();
    }

    @Override
    public CuentaResponse actualizar(String numeroCuenta, CuentaActualizacionRequest request) {
        Cuenta cuenta = buscarCuenta(numeroCuenta);
        cuenta.actualizar(request.tipoCuenta(), request.estado());
        return CuentaMapper.toResponse(cuentaRepository.guardar(cuenta));
    }

    @Override
    public CuentaResponse actualizarParcial(String numeroCuenta, CuentaParcialRequest request) {
        Cuenta cuenta = buscarCuenta(numeroCuenta);
        cuenta.actualizar(
                requireNonNullElse(request.tipoCuenta(), cuenta.getTipoCuenta()),
                requireNonNullElse(request.estado(), cuenta.isEstado()));
        return CuentaMapper.toResponse(cuentaRepository.guardar(cuenta));
    }

    private Cuenta buscarCuenta(String numeroCuenta) {
        return cuentaRepository.buscarPorNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta " + numeroCuenta + " no encontrada"));
    }
}
