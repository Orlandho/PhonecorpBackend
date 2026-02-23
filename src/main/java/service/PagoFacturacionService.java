package com.phonecorp.phonecorpbackend.service;

import com.phonecorp.phonecorpbackend.domain.*;
import com.phonecorp.phonecorpbackend.dto.*;
import com.phonecorp.phonecorpbackend.exception.HistorialCrediticioBloqueadoException;
import com.phonecorp.phonecorpbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

/**
 * BCE - Control: GestorPagoFacturacion
 * Procesa pagos validando historial crediticio y coordina la emision
 * del comprobante electronico con SUNAT/OSE (simulado).
 * @Transactional garantiza que pago y comprobante se persistan juntos o no se persistan.
 */
@Service
public class PagoFacturacionService {

    private final IPagoRepository        pagoRepository;
    private final IComprobanteRepository comprobanteRepository;
    private final IOrdenRepository       ordenRepository;
    private final SunatService           sunatService;

    @Autowired
    public PagoFacturacionService(IPagoRepository pagoRepository,
                                   IComprobanteRepository comprobanteRepository,
                                   IOrdenRepository ordenRepository,
                                   SunatService sunatService) {
        this.pagoRepository        = pagoRepository;
        this.comprobanteRepository = comprobanteRepository;
        this.ordenRepository       = ordenRepository;
        this.sunatService          = sunatService;
    }

    @Transactional
    public PagoResponseDTO procesarPago(PagoRequestDTO request) {

        EntidadOrdenVenta orden = ordenRepository.findById(request.getIdOrden())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Orden no encontrada con ID: " + request.getIdOrden()));

        EntidadCliente cliente = orden.getCliente();
        if ("MALO".equalsIgnoreCase(cliente.getHistorialCrediticio()))
            throw new HistorialCrediticioBloqueadoException(
                    "Pago bloqueado: el cliente '" + cliente.getNombresCompletos() +
                    "' tiene historial crediticio MALO.");

        if ("PAGADO".equalsIgnoreCase(orden.getEstado()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La orden " + request.getIdOrden() + " ya fue pagada.");

        EntidadPago pago = new EntidadPago();
        pago.setOrden(orden);
        pago.setMontoTotal(orden.getMontoTotal());
        pago.setMetodoPago(request.getMetodoPago());
        pago.setEstadoPago("COMPLETADO");
        EntidadPago pagoGuardado = pagoRepository.save(pago);

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

        orden.setEstado("PAGADO");
        ordenRepository.save(orden);

        return buildResponseDTO(pagoGuardado, comprobanteGuardado);
    }

    public PagoResponseDTO obtenerComprobante(Integer idPago) {
        EntidadPago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pago no encontrado con ID: " + idPago));
        EntidadComprobante comprobante = comprobanteRepository.buscarPorIdPago(idPago)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se encontro comprobante para el pago ID: " + idPago));
        return buildResponseDTO(pago, comprobante);
    }

    private PagoResponseDTO buildResponseDTO(EntidadPago pago, EntidadComprobante comprobante) {
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
