package com.phonecorp.phonecorpbackend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de salida para una orden de venta.
 * Incluye el desglose de subtotal, IGV (18%) y recargo por envío si aplica.
 */
public class OrdenVentaResponseDTO {

    private Integer idOrden;
    private Integer idCliente;
    private String nombreCliente;
    private String modalidadEntrega;
    private String estado;
    private LocalDateTime fechaEmision;
    private BigDecimal subtotalSinIgv;
    private BigDecimal igv;
    private BigDecimal recargoEnvio;
    private BigDecimal montoTotal;
    private List<ItemDetalleDTO> detalles;

    // ----- Inner DTO para los ítems del detalle -----
    public static class ItemDetalleDTO {
        private Integer idDetalle;
        private Integer idProducto;
        private String nombreProducto;
        private Integer cantidad;
        private BigDecimal precioPactado;
        private BigDecimal subtotal;

        public Integer getIdDetalle() { return idDetalle; }
        public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }

        public Integer getIdProducto() { return idProducto; }
        public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

        public BigDecimal getPrecioPactado() { return precioPactado; }
        public void setPrecioPactado(BigDecimal precioPactado) { this.precioPactado = precioPactado; }

        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    }

    public Integer getIdOrden() { return idOrden; }
    public void setIdOrden(Integer idOrden) { this.idOrden = idOrden; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getModalidadEntrega() { return modalidadEntrega; }
    public void setModalidadEntrega(String modalidadEntrega) { this.modalidadEntrega = modalidadEntrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public BigDecimal getSubtotalSinIgv() { return subtotalSinIgv; }
    public void setSubtotalSinIgv(BigDecimal subtotalSinIgv) { this.subtotalSinIgv = subtotalSinIgv; }

    public BigDecimal getIgv() { return igv; }
    public void setIgv(BigDecimal igv) { this.igv = igv; }

    public BigDecimal getRecargoEnvio() { return recargoEnvio; }
    public void setRecargoEnvio(BigDecimal recargoEnvio) { this.recargoEnvio = recargoEnvio; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public List<ItemDetalleDTO> getDetalles() { return detalles; }
    public void setDetalles(List<ItemDetalleDTO> detalles) { this.detalles = detalles; }
}
