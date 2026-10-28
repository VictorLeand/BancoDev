package com.banco.ms.clientes.presentation.controller;

import com.banco.ms.clientes.application.dto.ClienteActualizacionRequest;
import com.banco.ms.clientes.application.dto.ClienteParcialRequest;
import com.banco.ms.clientes.application.dto.ClienteRequest;
import com.banco.ms.clientes.application.dto.ClienteResponse;
import com.banco.ms.clientes.application.usecase.ClienteUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteUseCase clienteUseCase;

    public ClienteController(ClienteUseCase clienteUseCase) {
        this.clienteUseCase = clienteUseCase;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest request) {
        ClienteResponse creado = clienteUseCase.crear(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{clienteId}")
                .buildAndExpand(creado.clienteId())
                .toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteUseCase.listar();
    }

    @GetMapping("/{clienteId}")
    public ClienteResponse obtener(@PathVariable String clienteId) {
        return clienteUseCase.obtener(clienteId);
    }

    @PutMapping("/{clienteId}")
    public ClienteResponse actualizar(@PathVariable String clienteId,
                                      @Valid @RequestBody ClienteActualizacionRequest request) {
        return clienteUseCase.actualizar(clienteId, request);
    }

    @PatchMapping("/{clienteId}")
    public ClienteResponse actualizarParcial(@PathVariable String clienteId,
                                             @Valid @RequestBody ClienteParcialRequest request) {
        return clienteUseCase.actualizarParcial(clienteId, request);
    }

    @DeleteMapping("/{clienteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String clienteId) {
        clienteUseCase.eliminar(clienteId);
    }
}
