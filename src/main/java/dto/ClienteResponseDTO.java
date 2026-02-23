package com.phonecorp.phonecorpbackend.dto;

import java.time.LocalDateTime;

/** DTO de salida con los datos públicos de un cliente. */
public class ClienteResponseDTO {

    private Integer idCliente;
    private String dniCe;
    private String nombresCompletos;
    private String direccion;
    private String telefono;
    private String email;
    private String historialCrediticio;
    private LocalDateTime fechaRegistro;

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public String getDniCe() { return dniCe; }
    public void setDniCe(String dniCe) { this.dniCe = dniCe; }

    public String getNombresCompletos() { return nombresCompletos; }
    public void setNombresCompletos(String nombresCompletos) { this.nombresCompletos = nombresCompletos; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHistorialCrediticio() { return historialCrediticio; }
    public void setHistorialCrediticio(String historialCrediticio) { this.historialCrediticio = historialCrediticio; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
