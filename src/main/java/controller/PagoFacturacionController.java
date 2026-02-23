package com.phonecorp.phonecorpbackend.controller;

import com.phonecorp.phonecorpbackend.dto.PagoRequestDTO;
import com.phonecorp.phonecorpbackend.dto.PagoResponseDTO;
import com.phonecorp.phonecorpbackend.service.PagoFacturacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * BCE - Frontera: PagoFacturacionController
 * Recibe peticiones HTTP y delega toda la logica al PagoFacturacionService (Gestor).
 */
@RestController
@RequestMapping("/api/pagos")
public class PagoFacturacionController {

    private final PagoFacturacionService pagoFacturacionService;

    @Autowired
    public PagoFacturacionController(PagoFacturacionService pagoFacturacionService) {
        this.pagoFacturacionService = pagoFacturacionService;
    }

    @PostMapping("/procesar")
    @ResponseStatus(HttpStatus.CREATED)
    public PagoResponseDTO procesarPago(@RequestBody PagoRequestDTO request) {
        return pagoFacturacionService.procesarPago(request);
    }

    @GetMapping("/comprobante/{idPago}")
    public PagoResponseDTO obtenerComprobante(@PathVariable Integer idPago) {
        return pagoFacturacionService.obtenerComprobante(idPago);
    }
}
