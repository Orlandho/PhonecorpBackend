package config;

import org.springframework.context.annotation.Configuration;

/**
 * CLASE DE CONFIGURACIÓN: RestTemplateConfig
 * * Propósito: 
 * Proveer clientes HTTP configurados e inyectables para la comunicación con servicios externos.
 * * Responsabilidades y programación:
 * - Instanciar y exponer beans de RestTemplate o WebClient.
 * - Estos beans serán inyectados en los servicios correspondientes para realizar las peticiones
 * de validación de identidad hacia RENIEC y la emisión de comprobantes hacia SUNAT/OSE.
 * - Configurar *timeouts* (tiempos de espera) razonables para evitar que tu backend se cuelgue
 * si los servicios de SUNAT o RENIEC demoran en responder.
 */
@Configuration
public class RestTemplateConfig {
    // Aquí declararás los métodos anotados con @Bean que devuelvan instancias de RestTemplate.
}