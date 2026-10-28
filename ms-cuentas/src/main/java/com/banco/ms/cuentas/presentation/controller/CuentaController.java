package com.banco.ms.cuentas.presentation.controller;

import com.banco.ms.cuentas.application.dto.CuentaActualizacionRequest;
import com.banco.ms.cuentas.application.dto.CuentaParcialRequest;
import com.banco.ms.cuentas.application.dto.CuentaRequest;
import com.banco.ms.cuentas.application.dto.CuentaResponse;
import com.banco.ms.cuentas.application.usecase.CuentaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaUseCase cuentaUseCase;

    public CuentaController(CuentaUseCase cuentaUseCase) {
        this.cuentaUseCase = cuentaUseCase;
    }

    @PostMapping
    public ResponseEntity<CuentaResponse> crear(@Valid @RequestBody CuentaRequest request) {
        CuentaResponse creada = cuentaUseCase.crear(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{numeroCuenta}")
                .buildAndExpand(creada.numeroCuenta())
                .toUri();
        return ResponseEntity.created(location).body(creada);
    }

    @GetMapping
    public List<CuentaResponse> listar(@RequestParam(required = false) String clienteId) {
        return cuentaUseCase.listar(clienteId);
    }

    @GetMapping("/{numeroCuenta}")
    public CuentaResponse obtener(@PathVariable String numeroCuenta) {
        return cuentaUseCase.obtener(numeroCuenta);
    }

    @PutMapping("/{numeroCuenta}")
    public CuentaResponse actualizar(@PathVariable String numeroCuenta,
                                     @Valid @RequestBody CuentaActualizacionRequest request) {
        return cuentaUseCase.actualizar(numeroCuenta, request);
    }

    @PatchMapping("/{numeroCuenta}")
    public CuentaResponse actualizarParcial(@PathVariable String numeroCuenta,
                                            @Valid @RequestBody CuentaParcialRequest request) {
        return cuentaUseCase.actualizarParcial(numeroCuenta, request);
    }
}
