package exception;

/**
 * Excepción lanzada cuando el cliente tiene historial crediticio "MALO"
 * y no puede completar un pago.
 * Código de error: ERR_CREDITO_01 → HTTP 403 Forbidden
 */
public class HistorialCrediticioBloqueadoException extends RuntimeException {
    public HistorialCrediticioBloqueadoException(String mensaje) {
        super(mensaje);
    }
}
