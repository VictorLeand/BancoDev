package com.banco.ms.cuentas.application.usecase;

import com.banco.ms.cuentas.application.dto.ClienteEventoMensaje;

public interface SincronizarClienteUseCase {

    void sincronizar(ClienteEventoMensaje evento);
}
