package com.banco.ms.cuentas.application.service;

import com.banco.ms.cuentas.application.dto.ClienteEventoMensaje;
import com.banco.ms.cuentas.application.usecase.SincronizarClienteUseCase;
import com.banco.ms.cuentas.domain.model.ClienteSnapshot;
import com.banco.ms.cuentas.domain.model.Cuenta;
import com.banco.ms.cuentas.domain.port.ClienteSnapshotRepositoryPort;
import com.banco.ms.cuentas.domain.port.CuentaRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;



@Service
@Transactional
public class ClienteSincronizacionService implements SincronizarClienteUseCase {

    private static final Logger log = LoggerFactory.getLogger(ClienteSincronizacionService.class);

    private final ClienteSnapshotRepositoryPort clienteSnapshotRepository;
    private final CuentaRepositoryPort cuentaRepository;

    public ClienteSincronizacionService(ClienteSnapshotRepositoryPort clienteSnapshotRepository,
                                        CuentaRepositoryPort cuentaRepository) {
        this.clienteSnapshotRepository = clienteSnapshotRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public void sincronizar(ClienteEventoMensaje evento) {
        Optional<ClienteSnapshot> actual = clienteSnapshotRepository.buscarPorClienteId(evento.clienteId());
        if (actual.isPresent() && actual.get().esPosteriorA(evento.fecha())) {
            log.info("Evento {} del cliente {} descartado por ser anterior al estado actual",
                    evento.tipo(), evento.clienteId());
            return;
        }

        boolean eliminado = evento.tipo() == ClienteEventoMensaje.TipoEventoCliente.ELIMINADO;
        boolean activo = !eliminado && evento.estado();
        clienteSnapshotRepository.guardar(
                new ClienteSnapshot(evento.clienteId(), evento.nombre(), activo, evento.fecha()));

        if (eliminado) {
            desactivarCuentas(evento.clienteId());
        }
        log.info("Cliente {} sincronizado por evento {}", evento.clienteId(), evento.tipo());
    }

    private void desactivarCuentas(String clienteId) {
        for (Cuenta cuenta : cuentaRepository.buscarPorClienteId(clienteId)) {
            cuenta.desactivar();
            cuentaRepository.guardar(cuenta);
        }
    }
}
