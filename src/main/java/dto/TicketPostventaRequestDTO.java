package dto;

/** DTO de entrada para registrar un ticket de postventa. */
public class TicketPostventaRequestDTO {

    private Integer idCliente;
    private Integer idOrden;       // Opcional: puede ser null si es una consulta general
    private String motivo;         // ej: "RECLAMO", "CONSULTA", "GARANTIA"
    private String descripcionCaso;

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public Integer getIdOrden() { return idOrden; }
    public void setIdOrden(Integer idOrden) { this.idOrden = idOrden; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDescripcionCaso() { return descripcionCaso; }
    public void setDescripcionCaso(String descripcionCaso) { this.descripcionCaso = descripcionCaso; }
}
