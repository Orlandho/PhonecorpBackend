package domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * ENTIDAD - Cliente
 *
 * BCE → Representa la Entidad del dominio.
 *
 * SOLID - SRP (Responsabilidad Única):
 * Esta clase solo modela la estructura de datos persistente.
 * No contiene lógica de negocio ni validaciones complejas.
 */
@Entity
@Table(name = "Cliente")
public class EntidadCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SQL Server IDENTITY(1,1)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "dni_ce", nullable = false, unique = true, length = 20)
    private String dniCe;

    @Column(name = "nombres_completos", nullable = false, columnDefinition = "NVARCHAR(150)")
    private String nombresCompletos;

    @Column(name = "direccion", nullable = false, columnDefinition = "NVARCHAR(200)")
    private String direccion;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "historial_crediticio", length = 20)
    private String historialCrediticio;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    public EntidadCliente() {}

    // Getters & Setters (SRP: solo acceso al estado)

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