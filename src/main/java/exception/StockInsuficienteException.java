package exception;

/**
 * Excepción lanzada cuando un producto no tiene stock suficiente para la cantidad solicitada.
 * Código de error: ERR_STOCK_01 → HTTP 409 Conflict
 */
public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
