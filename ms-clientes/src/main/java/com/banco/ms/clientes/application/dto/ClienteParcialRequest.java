package com.banco.ms.clientes.application.dto;

import com.banco.ms.clientes.domain.model.Genero;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteParcialRequest(
        @Size(min = 1, max = 100) String nombre,
        Genero genero,
        @Min(0) @Max(150) Integer edad,
        @Size(min = 1, max = 20) String identificacion,
        @Size(min = 1, max = 200) String direccion,
        @Pattern(regexp = ValidacionPatrones.TELEFONO, message = ValidacionPatrones.TELEFONO_MENSAJE)
        String telefono,
        @Size(min = 4, max = 50) String contrasena,
        Boolean estado) {
}