package com.banco.ms.clientes.presentation.advice;

import com.banco.ms.clientes.domain.exception.RecursoDuplicadoException;
import com.banco.ms.clientes.domain.exception.RecursoNoEncontradoException;
import com.banco.ms.clientes.domain.exception.ValidacionDominioException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(RecursoNoEncontradoException ex, HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage(), request, List.of());
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErrorResponse> manejarDuplicado(RecursoDuplicadoException ex, HttpServletRequest request) {
        return construir(HttpStatus.CONFLICT, ex.getMessage(), request, List.of());
    }

    @ExceptionHandler(ValidacionDominioException.class)
    public ResponseEntity<ErrorResponse> manejarValidacionDominio(ValidacionDominioException ex, HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST, ex.getMessage(), request, List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .sorted()
                .toList();
        return construir(HttpStatus.BAD_REQUEST, "La petición contiene datos inválidos", request, detalles);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarCuerpoIlegible(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST, "El cuerpo de la petición es inválido o tiene un formato incorrecto",
                request, List.of());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorResponse> manejarParametroInvalido(Exception ex, HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST, "Parámetro inválido o faltante", request, List.of(ex.getMessage()));
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class,
            NoResourceFoundException.class})
    public ResponseEntity<ErrorResponse> manejarErrorProtocolo(Exception ex, HttpServletRequest request) {
        HttpStatusCode codigo = ((org.springframework.web.ErrorResponse) ex).getStatusCode();
        return construir(HttpStatus.valueOf(codigo.value()), ex.getMessage(), request, List.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> manejarIntegridad(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("Violación de integridad de datos: {}", ex.getMostSpecificCause().getMessage());
        return construir(HttpStatus.CONFLICT, "La operación viola una restricción de integridad de datos",
                request, List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarGeneral(Exception ex, HttpServletRequest request) {
        log.error("Error no controlado en {}", request.getRequestURI(), ex);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", request, List.of());
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus status, String mensaje,
                                                    HttpServletRequest request, List<String> detalles) {
        ErrorResponse body = new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
                mensaje, request.getRequestURI(), detalles);
        return ResponseEntity.status(status).body(body);
    }
}
