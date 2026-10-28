package com.banco.ms.cuentas.presentation.controller;

import com.banco.ms.cuentas.application.dto.MovimientoActualizacionRequest;
import com.banco.ms.cuentas.application.dto.MovimientoRequest;
import com.banco.ms.cuentas.application.dto.MovimientoResponse;
import com.banco.ms.cuentas.application.usecase.MovimientoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    private final MovimientoUseCase movimientoUseCase;

    public MovimientoController(MovimientoUseCase movimientoUseCase) {
        this.movimientoUseCase = movimientoUseCase;
    }

    @PostMapping
    public ResponseEntity<MovimientoResponse> registrar(@Valid @RequestBody MovimientoRequest request) {
        MovimientoResponse registrado = movimientoUseCase.registrar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(registrado.id())
                .toUri();
        return ResponseEntity.created(location).body(registrado);
    }

    @GetMapping
    public List<MovimientoResponse> listar(@RequestParam(required = false) String numeroCuenta) {
        return movimientoUseCase.listar(numeroCuenta);
    }

    @GetMapping("/{id}")
    public MovimientoResponse obtener(@PathVariable Long id) {
        return movimientoUseCase.obtener(id);
    }

    @PutMapping("/{id}")
    public MovimientoResponse actualizar(@PathVariable Long id,
                                         @Valid @RequestBody MovimientoActualizacionRequest request) {
        return movimientoUseCase.actualizar(id, request);
    }
}
