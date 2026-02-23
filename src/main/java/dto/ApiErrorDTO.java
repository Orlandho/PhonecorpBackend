package com.phonecorp.phonecorpbackend.dto;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para errores de la API.
 * Formato: { timestamp, mensaje, codigo }
 */
public class ApiErrorDTO {

    private LocalDateTime timestamp;
    private String mensaje;
    private String codigo;

    public ApiErrorDTO(String mensaje, String codigo) {
        this.timestamp = LocalDateTime.now();
        this.mensaje = mensaje;
        this.codigo = codigo;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getMensaje() { return mensaje; }
    public String getCodigo() { return codigo; }
}
