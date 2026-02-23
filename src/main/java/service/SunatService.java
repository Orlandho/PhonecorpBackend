package service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Simulación del servicio externo de SUNAT/OSE para emisión de comprobantes electrónicos.
 * Mock: genera hash UUID y correlativo automático.
 * En producción, este método llamaría al API de un OSE autorizado.
 *
 * @return String[] con { serie, numeroCorrelativo, hashSunat }
 */
@Service
public class SunatService {

    public String[] emitirComprobante(String tipoComprobante, BigDecimal monto) {
        String serie = "BOLETA".equalsIgnoreCase(tipoComprobante) ? "B001" : "F001";
        String correlativo = String.format("%08d", (int) (Math.random() * 99_999_999));
        String hash = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return new String[]{ serie, correlativo, hash };
    }
}
