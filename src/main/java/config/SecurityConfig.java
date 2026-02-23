package config;

import org.springframework.context.annotation.Configuration;

/**
 * CLASE DE CONFIGURACIÓN: SecurityConfig
 * * Propósito: 
 * Centralizar la configuración de Spring Security para proteger los endpoints REST del backend.
 * * Responsabilidades y programación:
 * - Implementar filtros de seguridad para interceptar todas las peticiones HTTP entrantes.
 * - Configurar la validación de tokens JWT (JSON Web Tokens) para asegurar que el usuario esté autenticado.
 * - Aplicar el control de acceso basado en roles (RNF-04), restringiendo rutas según el tipo de usuario.
 * - Por ejemplo: Solo el rol "INVENTARIO" podrá acceder a los endpoints de InventarioDespachoController, 
 * y el rol "FACTURACION" a los de PagoFacturacionController.
 * - Deshabilitar CSRF (Cross-Site Request Forgery) ya que se utilizará una arquitectura stateless con JWT.
 */
@Configuration
public class SecurityConfig {
    // Aquí definirás los SecurityFilterChain y los administradores de autenticación.
}