package controller;

import domain.EntidadProducto;
import repository.IProductoRepository;
import repository.IInventarioRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    private final IProductoRepository productoRepository;
    private final IInventarioRepository inventarioRepository;

    @Autowired
    public CatalogoController(IProductoRepository productoRepository,
                               IInventarioRepository inventarioRepository) {
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
    }

    /**
     * Lista todos los productos del catálogo.
     */
    @GetMapping("/productos")
    public List<EntidadProducto> listarProductos() {
        return productoRepository.findAll();
    }

    /**
     * Consulta el stock físico de un producto.
     */
    @GetMapping("/productos/{idProducto}/stock")
    public int consultarStock(@PathVariable Integer idProducto) {

        // Validar que el producto exista
        if (!productoRepository.existsById(idProducto)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Producto no encontrado con ID: " + idProducto
            );
        }

        // Si no existe inventario, retorna 0 (sin stock)
        return inventarioRepository.consultarStock(idProducto)
                .orElse(0);
    }
}