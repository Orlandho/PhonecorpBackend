package com.phonecorp.phonecorpbackend.controller;

import com.phonecorp.phonecorpbackend.domain.EntidadComprobante;
import com.phonecorp.phonecorpbackend.domain.EntidadPago;
import com.phonecorp.phonecorpbackend.repository.IComprobanteRepository;
import com.phonecorp.phonecorpbackend.repository.IPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * BCE - Control: GestorComprobante (auxiliar)
 * Permite consultar comprobantes. La creación se realiza a través de PagoFacturacionController.
 */
@RestController
@RequestMapping("/api/comprobantes")
public class ComprobanteController {

    private final IComprobanteRepository comprobanteRepository;
    private final IPagoRepository        pagoRepository;

    @Autowired
    public ComprobanteController(IComprobanteRepository comprobanteRepository,
                                  IPagoRepository pagoRepository) {
        this.comprobanteRepository = comprobanteRepository;
        this.pagoRepository        = pagoRepository;
    }

    @GetMapping
    public List<EntidadComprobante> listar() {
        return comprobanteRepository.findAll();
    }

    @GetMapping("/{idComprobante}")
    public EntidadComprobante obtenerPorId(@PathVariable Integer idComprobante) {
        return comprobanteRepository.findById(idComprobante)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Comprobante no encontrado con ID: " + idComprobante));
    }

    @GetMapping("/pago/{idPago}")
    public EntidadComprobante obtenerPorPago(@PathVariable Integer idPago) {
        return comprobanteRepository.buscarPorIdPago(idPago)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe comprobante para el pago ID: " + idPago));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntidadComprobante crear(@RequestBody EntidadComprobante c,
                                     @RequestParam Integer idPago) {

        if (c.getTipoComprobante() == null || c.getTipoComprobante().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tipoComprobante es obligatorio.");
        if (c.getSerie() == null || c.getSerie().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "serie es obligatorio.");
        if (c.getNumeroCorrelativo() == null || c.getNumeroCorrelativo().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "numeroCorrelativo es obligatorio.");

        EntidadPago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pago no encontrado con ID: " + idPago));

        if (comprobanteRepository.existePorIdPago(idPago))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un comprobante para el pago ID: " + idPago);

        if (comprobanteRepository.existePorCorrelativo(c.getTipoComprobante(), c.getSerie(), c.getNumeroCorrelativo()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe el comprobante: " + c.getTipoComprobante() + " " + c.getSerie() + "-" + c.getNumeroCorrelativo());

        c.setPago(pago);
        if (c.getFechaEmision() == null) c.setFechaEmision(LocalDateTime.now());

        return comprobanteRepository.save(c);
    }
}