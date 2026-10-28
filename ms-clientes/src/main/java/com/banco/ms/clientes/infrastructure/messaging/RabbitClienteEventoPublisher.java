package com.banco.ms.clientes.infrastructure.messaging;

import com.banco.ms.clientes.domain.event.ClienteEvento;
import com.banco.ms.clientes.domain.port.ClienteEventoPublisherPort;
import com.banco.ms.clientes.infrastructure.config.MensajeriaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;

@Component
public class RabbitClienteEventoPublisher implements ClienteEventoPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(RabbitClienteEventoPublisher.class);
    private static final String PREFIJO_ROUTING_KEY = "cliente.";

    private final RabbitTemplate rabbitTemplate;
    private final MensajeriaProperties properties;

    public RabbitClienteEventoPublisher(RabbitTemplate rabbitTemplate, MensajeriaProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    @Override
    public void publicar(ClienteEvento evento) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    enviar(evento);
                }
            });
        } else {
            enviar(evento);
        }
    }

    private void enviar(ClienteEvento evento) {
        String routingKey = PREFIJO_ROUTING_KEY + evento.tipo().name().toLowerCase(Locale.ROOT);
        try {
            rabbitTemplate.convertAndSend(properties.exchange(), routingKey, evento);
            log.info("Evento {} publicado para el cliente {}", evento.tipo(), evento.clienteId());
        } catch (AmqpException ex) {
            log.error("No se pudo publicar el evento {} del cliente {}", evento.tipo(), evento.clienteId(), ex);
        }
    }
}
