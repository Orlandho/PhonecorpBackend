package com.phonecorp.phonecorpbackend.exception;

import com.phonecorp.phonecorpbackend.dto.ApiErrorDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * Manejador global de excepciones (BCE - Frontera).
 * Centraliza todas las respuestas de error en el formato { timestamp, mensaje, codigo }.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ApiErrorDTO> handleStockInsuficiente(StockInsuficienteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorDTO(ex.getMessage(), "ERR_STOCK_01"));
    }

    @ExceptionHandler(DniInvalidoException.class)
    public ResponseEntity<ApiErrorDTO> handleDniInvalido(DniInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ApiErrorDTO(ex.getMessage(), "ERR_RENIEC_01"));
    }

    @ExceptionHandler(HistorialCrediticioBloqueadoException.class)
    public ResponseEntity<ApiErrorDTO> handleHistorialBloqueado(HistorialCrediticioBloqueadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiErrorDTO(ex.getMessage(), "ERR_CREDITO_01"));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorDTO> handleResponseStatus(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ApiErrorDTO(ex.getReason(), "ERR_HTTP_" + ex.getStatusCode().value()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorDTO(ex.getMessage(), "ERR_NOT_FOUND_01"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorDTO> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorDTO(ex.getMessage(), "ERR_VALIDACION_01"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorDTO("Error interno del servidor: " + ex.getMessage(), "ERR_INTERNO_01"));
    }
}
