package com.banco.ms.clientes.application.dto;

import com.banco.ms.clientes.domain.model.Genero;
import jakarta.validation.constraints.*;

public record ClienteRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotNull Genero genero,
        @NotNull @Min(0) @Max(150) Integer edad,
        @NotBlank @Size(max = 20) String identificacion,
        @NotBlank @Size(max = 200) String direccion,
        @NotBlank @Pattern(regexp = ValidacionPatrones.TELEFONO, message = ValidacionPatrones.TELEFONO_MENSAJE)
        String telefono,
        @NotBlank @Size(min = 4, max = 50) String contrasena,
        @NotNull Boolean estado) {
}
