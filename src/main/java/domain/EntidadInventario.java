package com.phonecorp.phonecorpbackend.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * BCE - Entidad Inventario.
 * SRP: solo representa la tabla Inventario. Cero lógica de negocio.
 * Relación JPA hacia EntidadProducto (ManyToOne).
 */
@Entity
@Table(name = "Inventario")
public class EntidadInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Integer idInventario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private EntidadProducto producto;

    @Column(name = "stock_fisico", nullable = false)
    private Integer stockFisico;

    @Column(name = "stock_comprometido")
    private Integer stockComprometido;

    @Column(name = "ubicacion_almacen", columnDefinition = "NVARCHAR(50)")
    private String ubicacionAlmacen;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public EntidadInventario() {}

    public Integer getIdInventario() { return idInventario; }
    public void setIdInventario(Integer idInventario) { this.idInventario = idInventario; }

    public EntidadProducto getProducto() { return producto; }
    public void setProducto(EntidadProducto producto) { this.producto = producto; }

    public Integer getStockFisico() { return stockFisico; }
    public void setStockFisico(Integer stockFisico) { this.stockFisico = stockFisico; }

    public Integer getStockComprometido() { return stockComprometido; }
    public void setStockComprometido(Integer stockComprometido) { this.stockComprometido = stockComprometido; }

    public String getUbicacionAlmacen() { return ubicacionAlmacen; }
    public void setUbicacionAlmacen(String ubicacionAlmacen) { this.ubicacionAlmacen = ubicacionAlmacen; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}