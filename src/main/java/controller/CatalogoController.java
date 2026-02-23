package com.phonecorp.phonecorpbackend.controller;

import com.phonecorp.phonecorpbackend.dto.ProductoResponseDTO;
import com.phonecorp.phonecorpbackend.service.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BCE - Frontera: CatalogoController
 * Recibe peticiones HTTP y delega toda la logica al CatalogoService (Gestor).
 */
@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    private final CatalogoService catalogoService;

    @Autowired
    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping("/productos")
    public List<ProductoResponseDTO> listarProductos() {
        return catalogoService.listarTodos();
    }

    @GetMapping("/productos/marca/{marca}")
    public List<ProductoResponseDTO> listarPorMarca(@PathVariable String marca) {
        return catalogoService.listarPorMarca(marca);
    }

    @GetMapping("/productos/{idProducto}")
    public ProductoResponseDTO obtenerProducto(@PathVariable Integer idProducto) {
        return catalogoService.obtenerConStock(idProducto);
    }

    @GetMapping("/productos/{idProducto}/stock")
    public int consultarStock(@PathVariable Integer idProducto) {
        return catalogoService.consultarStock(idProducto);
    }
}