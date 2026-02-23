package domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * BCE - Entidad TicketPostventa.
 * Gestión de postventa para resolver la manualidad del proceso actual.
 * Relaciones JPA hacia EntidadCliente (ManyToOne) y EntidadOrdenVenta (ManyToOne).
 */
@Entity
@Table(name = "TicketPostventa")
public class EntidadTicketPostventa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Integer idTicket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private EntidadCliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden")
    private EntidadOrdenVenta orden;

    @Column(nullable = false, length = 50)
    private String motivo;

    @Column(name = "descripcion_caso", length = 500)
    private String descripcionCaso;

    @Column(name = "estado_ticket", length = 20)
    private String estadoTicket = "ABIERTO";

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    public Integer getIdTicket() { return idTicket; }
    public void setIdTicket(Integer idTicket) { this.idTicket = idTicket; }

    public EntidadCliente getCliente() { return cliente; }
    public void setCliente(EntidadCliente cliente) { this.cliente = cliente; }

    public EntidadOrdenVenta getOrden() { return orden; }
    public void setOrden(EntidadOrdenVenta orden) { this.orden = orden; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDescripcionCaso() { return descripcionCaso; }
    public void setDescripcionCaso(String descripcionCaso) { this.descripcionCaso = descripcionCaso; }

    public String getEstadoTicket() { return estadoTicket; }
    public void setEstadoTicket(String estadoTicket) { this.estadoTicket = estadoTicket; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}