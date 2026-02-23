package exception;

/**
 * Excepción lanzada cuando el DNI/CE no supera la validación de RENIEC.
 * Código de error: ERR_RENIEC_01 → HTTP 422 Unprocessable Entity
 */
public class DniInvalidoException extends RuntimeException {
    public DniInvalidoException(String mensaje) {
        super(mensaje);
    }
}
