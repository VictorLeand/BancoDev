package com.banco.ms.cuentas;

import com.banco.ms.cuentas.application.dto.ClienteEventoMensaje;
import com.banco.ms.cuentas.application.usecase.SincronizarClienteUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MovimientoIntegrationTest {

    private static final String CLIENTE_ID = "A1B2C3D4E5F60718293A4B5C6D7E8F90";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SincronizarClienteUseCase sincronizarClienteUseCase;

    @Test
    void debeRegistrarMovimientosAlertarSaldoNoDisponibleYGenerarReporte() throws Exception {
        sincronizarClienteUseCase.sincronizar(new ClienteEventoMensaje(
                ClienteEventoMensaje.TipoEventoCliente.CREADO, CLIENTE_ID, "Marianela Montalvo", true, LocalDateTime.now()));

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numeroCuenta":"900001","tipoCuenta":"CORRIENTE","saldoInicial":100,
                                 "estado":true,"clienteId":"%s"}
                                """.formatted(CLIENTE_ID)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.saldoDisponible").value(100.0));

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numeroCuenta\":\"900001\",\"valor\":600}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoMovimiento").value("DEPOSITO"))
                .andExpect(jsonPath("$.saldo").value(700.0));

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numeroCuenta\":\"900001\",\"valor\":-700.01}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));

        mockMvc.perform(get("/api/cuentas/900001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldoDisponible").value(700.0));

        String hoy = LocalDate.now().toString();
        mockMvc.perform(get("/api/reportes")
                        .param("fecha", hoy + "," + hoy)
                        .param("cliente", CLIENTE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cliente").value("Marianela Montalvo"))
                .andExpect(jsonPath("$.cuentas", hasSize(1)))
                .andExpect(jsonPath("$.cuentas[0].saldoDisponible").value(700.0))
                .andExpect(jsonPath("$.cuentas[0].totalCreditos").value(600.0))
                .andExpect(jsonPath("$.cuentas[0].movimientos", hasSize(1)));
    }
}
