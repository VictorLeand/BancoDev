package com.banco.ms.cuentas.infrastructure.messaging;

import com.banco.ms.cuentas.application.dto.ClienteEventoMensaje;
import com.banco.ms.cuentas.application.usecase.SincronizarClienteUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;



@Component
public class ClienteEventoListener {

    private static final Logger log = LoggerFactory.getLogger(ClienteEventoListener.class);

    private final SincronizarClienteUseCase sincronizarClienteUseCase;

    public ClienteEventoListener(SincronizarClienteUseCase sincronizarClienteUseCase) {
        this.sincronizarClienteUseCase = sincronizarClienteUseCase;
    }

    @RabbitListener(queues = "${mensajeria.cola}")
    public void recibir(ClienteEventoMensaje evento) {
        log.info("Evento {} recibido para el cliente {}", evento.tipo(), evento.clienteId());
        sincronizarClienteUseCase.sincronizar(evento);
    }
}
