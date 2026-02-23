package config;

import org.springframework.context.annotation.Configuration;

/**
 * CLASE DE CONFIGURACIÓN: CorsConfig
 * * Propósito: 
 * Habilitar y gestionar las políticas de Intercambio de Recursos de Origen Cruzado (CORS).
 * * Responsabilidades y programación:
 * - Crear un bean de configuración (ej. WebMvcConfigurer) para permitir que el frontend web
 * (que correrá en un puerto o dominio distinto) pueda consumir esta API sin ser bloqueado por el navegador.
 * - Especificar explícitamente qué orígenes (orígenes del frontend), métodos HTTP (GET, POST, PUT, DELETE) 
 * y cabeceras (Authorization, Content-Type) están permitidos.
 */
@Configuration
public class CorsConfig {
    // Aquí definirás la configuración de mapeo CORS.
}