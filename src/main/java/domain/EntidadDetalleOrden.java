package com.phonecorp.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * ENTIDAD - DetalleOrden
 *
 * SOLID - SRP:
 * Representa una línea de detalle de una Orden de Venta.
 */
@Entity
@Table(
    name = "DetalleOrden",
    uniqueConstraints = {
        @UniqueConstraint(name = "UQ_Orden_Producto", columnNames = {"id_orden", "id_producto"})
    }
)
public class EntidadDetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @Column(name = "id_orden", nullable = false)
    private Integer idOrden;

    @Column(name = "id_producto", nullable = false)
    private Integer idProducto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    /**
     * DECIMAL(10,2) → BigDecimal
     * Evita errores financieros (principio de precisión monetaria).
     */
    @Column(name = "precio_pactado", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPactado;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    public DetalleOrden() {}

    // Getters & Setters

    public Integer getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }

    public Integer getIdOrden() { return idOrden; }
    public void setIdOrden(Integer idOrden) { this.idOrden = idOrden; }

    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioPactado() { return precioPactado; }
    public void setPrecioPactado(BigDecimal precioPactado) { this.precioPactado = precioPactado; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}