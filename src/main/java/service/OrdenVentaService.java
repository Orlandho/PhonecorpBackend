package com.phonecorp.phonecorpbackend.service;

import com.phonecorp.phonecorpbackend.domain.*;
import com.phonecorp.phonecorpbackend.dto.*;
import com.phonecorp.phonecorpbackend.exception.StockInsuficienteException;
import com.phonecorp.phonecorpbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BCE - Control: GestorOrdenVenta
 * Crea ordenes de venta validando stock, calculando IGV (18%)
 * y aplicando recargo de S/ 15.00 por Envio a Domicilio.
 * @Transactional garantiza rollback si cualquier paso de escritura falla.
 */
@Service
public class OrdenVentaService {

    private static final BigDecimal IGV          = new BigDecimal("0.18");
    private static final BigDecimal RECARGO_ENVIO = new BigDecimal("15.00");

    private final IOrdenRepository        ordenRepository;
    private final IClienteRepository      clienteRepository;
    private final IProductoRepository     productoRepository;
    private final IInventarioRepository   inventarioRepository;
    private final IDetalleOrdenRepository detalleOrdenRepository;

    @Autowired
    public OrdenVentaService(IOrdenRepository ordenRepository,
                              IClienteRepository clienteRepository,
                              IProductoRepository productoRepository,
                              IInventarioRepository inventarioRepository,
                              IDetalleOrdenRepository detalleOrdenRepository) {
        this.ordenRepository        = ordenRepository;
        this.clienteRepository      = clienteRepository;
        this.productoRepository     = productoRepository;
        this.inventarioRepository   = inventarioRepository;
        this.detalleOrdenRepository = detalleOrdenRepository;
    }

    @Transactional
    public OrdenVentaResponseDTO crearOrden(OrdenVentaRequestDTO request) {

        EntidadCliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + request.getIdCliente()));

        if (request.getItems() == null || request.getItems().isEmpty())
            throw new IllegalArgumentException("La orden debe contener al menos un item.");

        EntidadOrdenVenta orden = new EntidadOrdenVenta();
        orden.setCliente(cliente);
        orden.setModalidadEntrega(request.getModalidadEntrega());
        orden.setEstado("PENDIENTE");
        orden.setFechaEmision(LocalDateTime.now());
        orden.setMontoTotal(BigDecimal.ZERO);
        EntidadOrdenVenta ordenGuardada = ordenRepository.save(orden);

        BigDecimal subtotalBase = BigDecimal.ZERO;
        List<EntidadDetalleOrden> detalles = new ArrayList<>();

        for (ItemOrdenRequest item : request.getItems()) {
            EntidadProducto producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + item.getIdProducto()));

            int stockDisponible = inventarioRepository.consultarStock(item.getIdProducto()).orElse(0);
            if (stockDisponible < item.getCantidad())
                throw new StockInsuficienteException(
                        "Stock insuficiente para '" + producto.getNombre() +
                        "'. Disponible: " + stockDisponible + ", solicitado: " + item.getCantidad());

            BigDecimal subtotal = producto.getPrecioUnitario()
                    .multiply(new BigDecimal(item.getCantidad()))
                    .setScale(2, RoundingMode.HALF_UP);

            EntidadDetalleOrden detalle = new EntidadDetalleOrden();
            detalle.setOrden(ordenGuardada);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioPactado(producto.getPrecioUnitario());
            detalle.setSubtotal(subtotal);
            detalles.add(detalle);
            subtotalBase = subtotalBase.add(subtotal);
        }

        BigDecimal igvMonto    = subtotalBase.multiply(IGV).setScale(2, RoundingMode.HALF_UP);
        BigDecimal recargoEnvio = "Envío a Domicilio".equalsIgnoreCase(request.getModalidadEntrega())
                ? RECARGO_ENVIO : BigDecimal.ZERO;
        BigDecimal montoTotal  = subtotalBase.add(igvMonto).add(recargoEnvio).setScale(2, RoundingMode.HALF_UP);

        detalleOrdenRepository.saveAll(detalles);
        ordenGuardada.setMontoTotal(montoTotal);
        ordenRepository.save(ordenGuardada);

        return buildResponseDTO(ordenGuardada, detalles, subtotalBase, igvMonto, recargoEnvio, montoTotal);
    }

    public OrdenVentaResponseDTO obtenerOrden(Integer idOrden) {
        EntidadOrdenVenta orden = ordenRepository.findById(idOrden)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Orden no encontrada con ID: " + idOrden));
        List<EntidadDetalleOrden> detalles = detalleOrdenRepository.findByOrden_IdOrden(idOrden);
        BigDecimal subtotal = detalles.stream().map(EntidadDetalleOrden::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal igv     = subtotal.multiply(IGV).setScale(2, RoundingMode.HALF_UP);
        BigDecimal recargo = "Envío a Domicilio".equalsIgnoreCase(orden.getModalidadEntrega())
                ? RECARGO_ENVIO : BigDecimal.ZERO;
        return buildResponseDTO(orden, detalles, subtotal, igv, recargo, orden.getMontoTotal());
    }

    private OrdenVentaResponseDTO buildResponseDTO(EntidadOrdenVenta orden,
                                                    List<EntidadDetalleOrden> detalles,
                                                    BigDecimal subtotal, BigDecimal igv,
                                                    BigDecimal recargo, BigDecimal montoTotal) {
        OrdenVentaResponseDTO dto = new OrdenVentaResponseDTO();
        dto.setIdOrden(orden.getIdOrden());
        dto.setIdCliente(orden.getCliente().getIdCliente());
        dto.setNombreCliente(orden.getCliente().getNombresCompletos());
        dto.setModalidadEntrega(orden.getModalidadEntrega());
        dto.setEstado(orden.getEstado());
        dto.setFechaEmision(orden.getFechaEmision());
        dto.setSubtotalSinIgv(subtotal);
        dto.setIgv(igv);
        dto.setRecargoEnvio(recargo);
        dto.setMontoTotal(montoTotal);
        dto.setDetalles(detalles.stream().map(d -> {
            OrdenVentaResponseDTO.ItemDetalleDTO item = new OrdenVentaResponseDTO.ItemDetalleDTO();
            item.setIdDetalle(d.getIdDetalle());
            item.setIdProducto(d.getProducto().getIdProducto());
            item.setNombreProducto(d.getProducto().getNombre());
            item.setCantidad(d.getCantidad());
            item.setPrecioPactado(d.getPrecioPactado());
            item.setSubtotal(d.getSubtotal());
            return item;
        }).collect(Collectors.toList()));
        return dto;
    }
}
