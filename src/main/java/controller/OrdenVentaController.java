package com.phonecorp.phonecorpbackend.controller;

import com.phonecorp.phonecorpbackend.dto.OrdenVentaRequestDTO;
import com.phonecorp.phonecorpbackend.dto.OrdenVentaResponseDTO;
import com.phonecorp.phonecorpbackend.service.OrdenVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * BCE - Frontera: OrdenVentaController
 * Recibe peticiones HTTP y delega toda la logica al OrdenVentaService (Gestor).
 */
@RestController
@RequestMapping("/api/ordenes")
public class OrdenVentaController {

    private final OrdenVentaService ordenVentaService;

    @Autowired
    public OrdenVentaController(OrdenVentaService ordenVentaService) {
        this.ordenVentaService = ordenVentaService;
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public OrdenVentaResponseDTO crearOrden(@RequestBody OrdenVentaRequestDTO request) {
        return ordenVentaService.crearOrden(request);
    }

    @GetMapping("/{idOrden}")
    public OrdenVentaResponseDTO obtenerOrden(@PathVariable Integer idOrden) {
        return ordenVentaService.obtenerOrden(idOrden);
    }
}
