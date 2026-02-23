package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * BCE - Entidad OrdenVenta.
 * SRP: solo representa la estructura persistente.
 * Relaciones JPA hacia EntidadCliente (ManyToOne) y EntidadDetalleOrden (OneToMany).
 */
@Entity
@Table(name = "OrdenVenta")
public class EntidadOrdenVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Integer idOrden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private EntidadCliente cliente;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "modalidad_entrega", nullable = false, length = 50)
    private String modalidadEntrega;

    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    // @JsonIgnore evita recursión infinita al serializar la relación bidireccional
    @JsonIgnore
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EntidadDetalleOrden> detalles = new ArrayList<>();

    public EntidadOrdenVenta() {}

    public Integer getIdOrden() { return idOrden; }
    public void setIdOrden(Integer idOrden) { this.idOrden = idOrden; }

    public EntidadCliente getCliente() { return cliente; }
    public void setCliente(EntidadCliente cliente) { this.cliente = cliente; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getModalidadEntrega() { return modalidadEntrega; }
    public void setModalidadEntrega(String modalidadEntrega) { this.modalidadEntrega = modalidadEntrega; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public List<EntidadDetalleOrden> getDetalles() { return detalles; }
    public void setDetalles(List<EntidadDetalleOrden> detalles) { this.detalles = detalles; }
}