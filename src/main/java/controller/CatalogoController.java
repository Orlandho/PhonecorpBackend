package controller;

import domain.EntidadProducto;
import repository.IProductoRepository;
import repository.IInventarioRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Controlador: GestorCatalogo
 * Responsabilidad: Mostrar catálogo y consultar stock.
 * Dependencias: IProductoRepository, IInventarioRepository.
 */
@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    private final IProductoRepository productoRepository;
    private final IInventarioRepository inventarioRepository;

    // Inyección de dependencias mediante constructor para mantener bajo acoplamiento
    @Autowired
    public CatalogoController(IProductoRepository productoRepository, IInventarioRepository inventarioRepository) {
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
    }

    /**
     * Metodo listarProductos
     * Retorna la lista de smartphones disponibles en el catálogo.
     */
    @GetMapping("/productos")
    public List<EntidadProducto> listarProductos() {
        return productoRepository.findAll();
    }

    /**
     * Metodo consultarStock
     * Recibe el identificador del producto y consulta su disponibilidad física en el Kardex.
     */
    @GetMapping("/productos/{idProducto}/stock")
    public int consultarStock(@PathVariable Integer idProducto) {
        // Se asume que IInventarioRepository tiene el método declarado en el diagrama de diseño
        return inventarioRepository.consultarStock(idProducto);
    }
}