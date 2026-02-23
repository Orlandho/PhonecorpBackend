package controller;

import domain.EntidadProducto;
import dto.ProductoResponseDTO;
import repository.IInventarioRepository;
import repository.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * BCE - Control: GestorCatalogo
 * Lista y filtra productos verificando disponibilidad real en inventario.
 */
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

    @GetMapping("/productos")
    public List<ProductoResponseDTO> listarProductos() {
        return productoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/productos/marca/{marca}")
    public List<ProductoResponseDTO> listarPorMarca(@PathVariable String marca) {
        return productoRepository.findAll().stream()
                .filter(p -> marca.equalsIgnoreCase(p.getMarca()))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /** Retorna el detalle del producto verificando que haya stock físico disponible. */
    @GetMapping("/productos/{idProducto}")
    public ProductoResponseDTO obtenerProducto(@PathVariable Integer idProducto) {
        EntidadProducto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + idProducto));

        int stock = inventarioRepository.consultarStock(idProducto).orElse(0);
        if (stock == 0)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El producto '" + producto.getNombre() + "' no tiene stock disponible.");

        ProductoResponseDTO dto = toResponseDTO(producto);
        dto.setStockFisico(stock);
        return dto;
    }

    @GetMapping("/productos/{idProducto}/stock")
    public int consultarStock(@PathVariable Integer idProducto) {
        if (!productoRepository.existsById(idProducto))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Producto no encontrado con ID: " + idProducto);
        return inventarioRepository.consultarStock(idProducto).orElse(0);
    }

    private ProductoResponseDTO toResponseDTO(EntidadProducto p) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setIdProducto(p.getIdProducto());
        dto.setSku(p.getSku());
        dto.setNombre(p.getNombre());
        dto.setMarca(p.getMarca());
        dto.setModelo(p.getModelo());
        dto.setPrecioUnitario(p.getPrecioUnitario());
        dto.setDescripcion(p.getDescripcion());
        dto.setStockFisico(inventarioRepository.consultarStock(p.getIdProducto()).orElse(0));
        return dto;
    }
}