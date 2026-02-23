package com.phonecorp.phonecorpbackend.dto;

/** DTO de entrada para procesar un pago y emitir comprobante. */
public class PagoRequestDTO {

    private Integer idOrden;
    private String metodoPago;       // ej: "EFECTIVO", "TARJETA", "TRANSFERENCIA"
    private String tipoComprobante;  // "BOLETA" o "FACTURA"

    public Integer getIdOrden() { return idOrden; }
    public void setIdOrden(Integer idOrden) { this.idOrden = idOrden; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getTipoComprobante() { return tipoComprobante; }
    public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }
}
