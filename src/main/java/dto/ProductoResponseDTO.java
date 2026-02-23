package dto;

import java.math.BigDecimal;

/** DTO de salida para un producto del catálogo, incluyendo stock físico disponible. */
public class ProductoResponseDTO {

    private Integer idProducto;
    private String sku;
    private String nombre;
    private String marca;
    private String modelo;
    private BigDecimal precioUnitario;
    private String descripcion;
    private Integer stockFisico;

    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getStockFisico() { return stockFisico; }
    public void setStockFisico(Integer stockFisico) { this.stockFisico = stockFisico; }
}
