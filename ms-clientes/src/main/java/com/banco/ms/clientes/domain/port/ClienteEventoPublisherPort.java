package com.banco.ms.clientes.domain.port;

import com.banco.ms.clientes.domain.event.ClienteEvento;

public interface ClienteEventoPublisherPort {

    void publicar(ClienteEvento evento);
}