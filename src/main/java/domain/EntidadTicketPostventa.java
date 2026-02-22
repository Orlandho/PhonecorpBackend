package com.phonecorp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Segun el informe: Gestion de postventa para resolver la manualidad del proceso actual
@Entity
@Table(name = "TicketPostventa")
public class EntidadTicketPostventa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Integer idTicket;

    @Column(name = "id_cliente", nullable = false)
    private Integer idCliente;

    @Column(name = "id_orden")
    private Integer idOrden;

    @Column(nullable = false, length = 50)
    private String motivo;

    @Column(name = "descripcion_caso", length = 500)
    private String descripcionCaso;

    // Se asegura que el estado coincida con los flujos definidos en el informe
    @Column(name = "estado_ticket", length = 20)
    private String estadoTicket = "ABIERTO";

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    // Getters y Setters...
    public Integer getIdTicket() { return idTicket; }
    public void setIdTicket(Integer idTicket) { this.idTicket = idTicket; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public Integer getIdOrden() { return idOrden; }
    public void setIdOrden(Integer idOrden) { this.idOrden = idOrden; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDescripcionCaso() { return descripcionCaso; }
    public void setDescripcionCaso(String descripcionCaso) { this.descripcionCaso = descripcionCaso; }

    public String getEstadoTicket() { return estadoTicket; }
    public void setEstadoTicket(String estadoTicket) { this.estadoTicket = estadoTicket; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}