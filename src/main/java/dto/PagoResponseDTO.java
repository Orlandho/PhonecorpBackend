package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** DTO de salida con el resumen del pago y el comprobante emitido por SUNAT (simulado). */
public class PagoResponseDTO {

    // Datos del Pago
    private Integer idPago;
    private Integer idOrden;
    private BigDecimal montoTotal;
    private String metodoPago;
    private String estadoPago;
    private LocalDateTime fechaPago;

    // Datos del Comprobante
    private Integer idComprobante;
    private String tipoComprobante;
    private String serie;
    private String numeroCorrelativo;
    private String hashSunat;
    private LocalDateTime fechaEmisionComprobante;

    public Integer getIdPago() { return idPago; }
    public void setIdPago(Integer idPago) { this.idPago = idPago; }

    public Integer getIdOrden() { return idOrden; }
    public void setIdOrden(Integer idOrden) { this.idOrden = idOrden; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }

    public Integer getIdComprobante() { return idComprobante; }
    public void setIdComprobante(Integer idComprobante) { this.idComprobante = idComprobante; }

    public String getTipoComprobante() { return tipoComprobante; }
    public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }

    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }

    public String getNumeroCorrelativo() { return numeroCorrelativo; }
    public void setNumeroCorrelativo(String numeroCorrelativo) { this.numeroCorrelativo = numeroCorrelativo; }

    public String getHashSunat() { return hashSunat; }
    public void setHashSunat(String hashSunat) { this.hashSunat = hashSunat; }

    public LocalDateTime getFechaEmisionComprobante() { return fechaEmisionComprobante; }
    public void setFechaEmisionComprobante(LocalDateTime fechaEmisionComprobante) {
        this.fechaEmisionComprobante = fechaEmisionComprobante;
    }
}
