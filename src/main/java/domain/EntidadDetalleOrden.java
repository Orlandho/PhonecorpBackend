package com.phonecorp.phonecorpbackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * BCE - Entidad DetalleOrden.
 * SRP: representa una línea de detalle de una Orden de Venta.
 * Relaciones JPA hacia EntidadOrdenVenta (ManyToOne) y EntidadProducto (ManyToOne).
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

    // @JsonIgnore en la back-reference para evitar recursión infinita
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden", nullable = false)
    private EntidadOrdenVenta orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private EntidadProducto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    /** DECIMAL(10,2) → BigDecimal para precisión monetaria. */
    @Column(name = "precio_pactado", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPactado;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    public EntidadDetalleOrden() {}

    public Integer getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }

    public EntidadOrdenVenta getOrden() { return orden; }
    public void setOrden(EntidadOrdenVenta orden) { this.orden = orden; }

    public EntidadProducto getProducto() { return producto; }
    public void setProducto(EntidadProducto producto) { this.producto = producto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioPactado() { return precioPactado; }
    public void setPrecioPactado(BigDecimal precioPactado) { this.precioPactado = precioPactado; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}