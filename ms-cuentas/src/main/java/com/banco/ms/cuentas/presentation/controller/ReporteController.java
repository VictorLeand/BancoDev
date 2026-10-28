package com.banco.ms.cuentas.presentation.controller;

import com.banco.ms.cuentas.application.dto.EstadoCuentaResponse;
import com.banco.ms.cuentas.application.usecase.ReporteUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteUseCase reporteUseCase;

    public ReporteController(ReporteUseCase reporteUseCase) {
        this.reporteUseCase = reporteUseCase;
    }



    @GetMapping
    public EstadoCuentaResponse estadoCuenta(@RequestParam("fecha") String fecha,
                                             @RequestParam("cliente") String cliente) {
        return reporteUseCase.generarEstadoCuenta(cliente, fecha);
    }
}
