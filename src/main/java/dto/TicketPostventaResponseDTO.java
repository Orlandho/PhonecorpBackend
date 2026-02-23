package dto;

import java.time.LocalDateTime;

/** DTO de salida con los datos del ticket de postventa. */
public class TicketPostventaResponseDTO {

    private Integer idTicket;
    private Integer idCliente;
    private String nombreCliente;
    private Integer idOrden;
    private String motivo;
    private String descripcionCaso;
    private String estadoTicket;
    private LocalDateTime fechaRegistro;

    public Integer getIdTicket() { return idTicket; }
    public void setIdTicket(Integer idTicket) { this.idTicket = idTicket; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

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
