package com.phonecorp.phonecorpbackend.controller;

import com.phonecorp.phonecorpbackend.service.InventarioDespachoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * BCE - Frontera: InventarioDespachoController
 * Recibe peticiones HTTP y delega toda la logica al InventarioDespachoService (Gestor).
 */
@RestController
@RequestMapping("/api/inventario")
public class InventarioDespachoController {

    private final InventarioDespachoService inventarioDespachoService;

    @Autowired
    public InventarioDespachoController(InventarioDespachoService inventarioDespachoService) {
        this.inventarioDespachoService = inventarioDespachoService;
    }

    @PostMapping("/despachar/{idOrden}")
    public Map<String, Object> despacharOrden(@PathVariable Integer idOrden) {
        return inventarioDespachoService.despacharOrden(idOrden);
    }

    @GetMapping("/stock/{idProducto}")
    public Map<String, Object> consultarStock(@PathVariable Integer idProducto) {
        return inventarioDespachoService.consultarStock(idProducto);
    }
}
