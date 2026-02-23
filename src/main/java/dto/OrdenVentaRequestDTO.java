package com.phonecorp.phonecorpbackend.dto;

import java.util.List;

/** DTO de entrada para crear una orden de venta. */
public class OrdenVentaRequestDTO {

    private Integer idCliente;
    private String modalidadEntrega; // "Recojo en Tienda" o "Envío a Domicilio"
    private List<ItemOrdenRequest> items;

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public String getModalidadEntrega() { return modalidadEntrega; }
    public void setModalidadEntrega(String modalidadEntrega) { this.modalidadEntrega = modalidadEntrega; }

    public List<ItemOrdenRequest> getItems() { return items; }
    public void setItems(List<ItemOrdenRequest> items) { this.items = items; }
}
