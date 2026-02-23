package com.phonecorp.phonecorpbackend.dto;

/** DTO de entrada para un ítem (línea de producto) al crear una orden de venta. */
public class ItemOrdenRequest {

    private Integer idProducto;
    private Integer cantidad;

    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
