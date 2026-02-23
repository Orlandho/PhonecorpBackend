package config;

/**
 * CLASE DE CONFIGURACIÓN: ConexionBD
 * * Propósito: 
 * Garantizar una única instancia de la conexión a la base de datos SQL Server mediante el patrón Singleton.
 * * Responsabilidades y programación:
 * - Declarar un constructor privado para evitar la instanciación externa mediante la palabra clave 'new'.
 * - Contener una variable estática y privada de la misma clase (la instancia única).
 * - Exponer un método público y estático (ej. getInstance()) que devuelva la instancia.
 * - Nota técnica: Aunque Spring Boot gestiona nativamente el pool de conexiones a través de HikariCP 
 * (configurado en application.properties), esta clase se implementa para cumplir estrictamente 
 * con la matriz de patrones de diseño del proyecto.
 */
public class ConexionBD {
    // Aquí implementarás la lógica clásica del patrón Singleton.
}