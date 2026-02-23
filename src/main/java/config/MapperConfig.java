package config;

import org.springframework.context.annotation.Configuration;

/**
 * CLASE DE CONFIGURACIÓN: MapperConfig
 * * Propósito: 
 * Configurar e inicializar librerías de mapeo de objetos (como ModelMapper o MapStruct).
 * * Responsabilidades y programación:
 * - Exponer un bean de configuración centralizado.
 * - Definir reglas de conversión personalizadas si los nombres de los atributos varían 
 * entre los Data Transfer Objects (DTO) recibidos del cliente y tus entidades de dominio.
 * - Por ejemplo: Convertir automáticamente un 'ItemOrdenRequest' (JSON que llega al controlador) 
 * en una 'EntidadDetalleOrden' (Objeto que va a la base de datos), evitando mapeos manuales repetitivos.
 */
@Configuration
public class MapperConfig {
    // Aquí definirás el @Bean para ModelMapper u otra librería de transformación.
}