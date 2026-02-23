package controller;

import domain.*;
import dto.*;
import exception.HistorialCrediticioBloqueadoException;
import repository.*;
import service.SunatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

/**
 * BCE - Control: GestorPagoFacturacion
 * Procesa pagos validando historial crediticio del cliente
 * y coordina la emisión del comprobante electrónico con SUNAT/OSE (simulado).
 */
@RestController
@RequestMapping("/api/pagos")
public class PagoFacturacionController {

    private final IPagoRepository        pagoRepository;
    private final IComprobanteRepository comprobanteRepository;
    private final IOrdenRepository       ordenRepository;
    private final SunatService           sunatService;

    @Autowired
    public PagoFacturacionController(IPagoRepository pagoRepository,
                                      IComprobanteRepository comprobanteRepository,
                                      IOrdenRepository ordenRepository,
                                      SunatService sunatService) {
        this.pagoRepository        = pagoRepository;
        this.comprobanteRepository = comprobanteRepository;
        this.ordenRepository       = ordenRepository;
        this.sunatService          = sunatService;
    }

    @PostMapping("/procesar")
    @ResponseStatus(HttpStatus.CREATED)
    public PagoResponseDTO procesarPago(@RequestBody PagoRequestDTO request) {

        EntidadOrdenVenta orden = ordenRepository.findById(request.getIdOrden())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Orden no encontrada con ID: " + request.getIdOrden()));

        // Validar historial crediticio del cliente
        EntidadCliente cliente = orden.getCliente();
        if ("MALO".equalsIgnoreCase(cliente.getHistorialCrediticio()))
            throw new HistorialCrediticioBloqueadoException(
                    "Pago bloqueado: el cliente '" + cliente.getNombresCompletos() +
                    "' tiene historial crediticio MALO.");

        if ("PAGADO".equalsIgnoreCase(orden.getEstado()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La orden " + request.getIdOrden() + " ya fue pagada.");

        // Registrar pago
        EntidadPago pago = new EntidadPago();
        pago.setOrden(orden);
        pago.setMontoTotal(orden.getMontoTotal());
        pago.setMetodoPago(request.getMetodoPago());
        pago.setEstadoPago("COMPLETADO");
        EntidadPago pagoGuardado = pagoRepository.save(pago);

        // Emitir comprobante electrónico vía SUNAT (simulado)
        String[] datosSunat = sunatService.emitirComprobante(
                request.getTipoComprobante(), orden.getMontoTotal());

        EntidadComprobante comprobante = new EntidadComprobante();
        comprobante.setPago(pagoGuardado);
        comprobante.setTipoComprobante(request.getTipoComprobante());
        comprobante.setSerie(datosSunat[0]);
        comprobante.setNumeroCorrelativo(datosSunat[1]);
        comprobante.setHashSunat(datosSunat[2]);
        comprobante.setFechaEmision(LocalDateTime.now());
        EntidadComprobante comprobanteGuardado = comprobanteRepository.save(comprobante);

        // Actualizar estado de la orden
        orden.setEstado("PAGADO");
        ordenRepository.save(orden);

        return toResponseDTO(pagoGuardado, comprobanteGuardado);
    }

    @GetMapping("/comprobante/{idPago}")
    public PagoResponseDTO obtenerComprobante(@PathVariable Integer idPago) {
        EntidadPago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pago no encontrado con ID: " + idPago));
        EntidadComprobante comprobante = comprobanteRepository.buscarPorIdPago(idPago)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se encontró comprobante para el pago ID: " + idPago));
        return toResponseDTO(pago, comprobante);
    }

    private PagoResponseDTO toResponseDTO(EntidadPago pago, EntidadComprobante comprobante) {
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setIdPago(pago.getIdPago());
        dto.setIdOrden(pago.getOrden().getIdOrden());
        dto.setMontoTotal(pago.getMontoTotal());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setEstadoPago(pago.getEstadoPago());
        dto.setFechaPago(pago.getFechaPago());
        dto.setIdComprobante(comprobante.getIdComprobante());
        dto.setTipoComprobante(comprobante.getTipoComprobante());
        dto.setSerie(comprobante.getSerie());
        dto.setNumeroCorrelativo(comprobante.getNumeroCorrelativo());
        dto.setHashSunat(comprobante.getHashSunat());
        dto.setFechaEmisionComprobante(comprobante.getFechaEmision());
        return dto;
    }
}
