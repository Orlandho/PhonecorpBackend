package domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * ENTIDAD - Comprobante
 *
 * SOLID - SRP:
 * Representa únicamente la estructura persistente de la tabla Comprobante.
 */
@Entity
@Table(name = "Comprobante")
public class EntidadComprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comprobante")
    private Integer idComprobante;

    /**
     * Relación 1:1 con Pago
     * UNIQUE en BD.
     *
     * SOLID - OCP:
     * Si cambia la relación futura, solo modificamos esta capa de mapeo.
     */
    @Column(name = "id_pago", nullable = false, unique = true)
    private Integer idPago;

    @Column(name = "tipo_comprobante", nullable = false, length = 20)
    private String tipoComprobante;

    @Column(name = "serie", nullable = false, length = 10)
    private String serie;

    @Column(name = "numero_correlativo", nullable = false, length = 20)
    private String numeroCorrelativo;

    @Column(name = "hash_sunat", columnDefinition = "NVARCHAR(255)")
    private String hashSunat;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;

    public Comprobante() {}

    // Getters & Setters

    public Integer getIdComprobante() { return idComprobante; }
    public void setIdComprobante(Integer idComprobante) { this.idComprobante = idComprobante; }

    public Integer getIdPago() { return idPago; }
    public void setIdPago(Integer idPago) { this.idPago = idPago; }

    public String getTipoComprobante() { return tipoComprobante; }
    public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }

    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }

    public String getNumeroCorrelativo() { return numeroCorrelativo; }
    public void setNumeroCorrelativo(String numeroCorrelativo) { this.numeroCorrelativo = numeroCorrelativo; }

    public String getHashSunat() { return hashSunat; }
    public void setHashSunat(String hashSunat) { this.hashSunat = hashSunat; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
}