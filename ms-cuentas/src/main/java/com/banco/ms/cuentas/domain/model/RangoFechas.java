package com.banco.ms.cuentas.domain.model;

import com.banco.ms.cuentas.domain.exception.ValidacionDominioException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;


public record RangoFechas(LocalDate inicio, LocalDate fin) {

    private static final String FORMATO_ESPERADO = "Use el formato yyyy-MM-dd,yyyy-MM-dd";

    public RangoFechas {
        if (inicio == null || fin == null) {
            throw new ValidacionDominioException("El rango de fechas es obligatorio. " + FORMATO_ESPERADO);
        }
        if (inicio.isAfter(fin)) {
            throw new ValidacionDominioException("La fecha inicial no puede ser posterior a la fecha final");
        }
    }

    public static RangoFechas desdeTexto(String rango) {
        if (rango == null || rango.isBlank()) {
            throw new ValidacionDominioException("El parámetro fecha es obligatorio. " + FORMATO_ESPERADO);
        }
        String[] partes = rango.split(",");
        if (partes.length > 2) {
            throw new ValidacionDominioException("Rango de fechas inválido. " + FORMATO_ESPERADO);
        }
        try {
            LocalDate inicio = LocalDate.parse(partes[0].trim());
            LocalDate fin = partes.length == 2 ? LocalDate.parse(partes[1].trim()) : inicio;
            return new RangoFechas(inicio, fin);
        } catch (DateTimeParseException ex) {
            throw new ValidacionDominioException("Fecha inválida: '" + ex.getParsedString() + "'. " + FORMATO_ESPERADO);
        }
    }

    public LocalDateTime desde() {
        return inicio.atStartOfDay();
    }

    public LocalDateTime hastaExclusivo() {
        return fin.plusDays(1).atStartOfDay();
    }
}