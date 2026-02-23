package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    // 1. Variable estática y privada que almacenará la única instancia de la clase
    private static ConexionBD instancia;
    
    // Objeto de conexión nativo de Java
    private Connection conexion;

    // Credenciales para Autenticación de Windows
    private final String URL = "jdbc:sqlserver://localhost:1433;databaseName=DB_PhoneCorp;integratedSecurity=true;encrypt=true;trustServerCertificate=true";

    /**
     * 2. Constructor privado para evitar que otras clases creen instancias con 'new'
     */
    private ConexionBD() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.conexion = DriverManager.getConnection(URL);
            System.out.println("Conexión a DB_PhoneCorp establecida exitosamente mediante Autenticación de Windows.");
        } catch (ClassNotFoundException e) {
            System.out.println("Error de Driver: No se encontró el driver de SQL Server.");
        } catch (SQLException e) {
            System.out.println("Error de Conexión: " + e.getMessage());
        }
    }

    /**
     * 3. Método público y estático que actúa como punto de acceso global a la instancia
     * @return La instancia única de ConexionBD
     */
    public static ConexionBD getInstance() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    /**
     * Método para obtener el objeto Connection y ejecutar consultas
     */
    public Connection getConexion() {
        return conexion;
    }

    /**
     * Método para liberar los recursos manualmente si fuera necesario
     */
    public void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    
}