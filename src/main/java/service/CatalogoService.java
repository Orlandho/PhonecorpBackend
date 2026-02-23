package com.phonecorp.phonecorpbackend.service;

import com.phonecorp.phonecorpbackend.domain.EntidadInventario;
import com.phonecorp.phonecorpbackend.domain.EntidadProducto;
import com.phonecorp.phonecorpbackend.dto.ProductoResponseDTO;
import com.phonecorp.phonecorpbackend.repository.IInventarioRepository;
import com.phonecorp.phonecorpbackend.repository.IProductoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * BCE - Control: GestorCatalogo
 * Lista y filtra productos verificando disponibilidad real en inventario.
 * Usa consulta batch para evitar el problema N+1.
 */
@Service
public class CatalogoService {

    private final IProductoRepository   productoRepository;
    private final IInventarioRepository inventarioRepository;
    private final ModelMapper           mapper;

    @Autowired
    public CatalogoService(IProductoRepository productoRepository,
                            IInventarioRepository inventarioRepository,
                            ModelMapper mapper) {
        this.productoRepository   = productoRepository;
        this.inventarioRepository = inventarioRepository;
        this.mapper               = mapper;
    }

    public List<ProductoResponseDTO> listarTodos() {
        List<EntidadProducto> productos = productoRepository.findAll();
        return toResponseDTOList(productos);
    }

    public List<ProductoResponseDTO> listarPorMarca(String marca) {
        List<EntidadProducto> productos = productoRepository.findAll().stream()
                .filter(p -> marca.equalsIgnoreCase(p.getMarca()))
                .collect(Collectors.toList());
        return toResponseDTOList(productos);
    }

    public ProductoResponseDTO obtenerConStock(Integer idProducto) {
        EntidadProducto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + idProducto));

        int stock = inventarioRepository.consultarStock(idProducto).orElse(0);
        if (stock == 0)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El producto '" + producto.getNombre() + "' no tiene stock disponible.");

        ProductoResponseDTO dto = mapper.map(producto, ProductoResponseDTO.class);
        dto.setStockFisico(stock);
        return dto;
    }

    public int consultarStock(Integer idProducto) {
        if (!productoRepository.existsById(idProducto))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Producto no encontrado con ID: " + idProducto);
        return inventarioRepository.consultarStock(idProducto).orElse(0);
    }

    /**
     * Convierte una lista de productos a DTOs usando una sola consulta batch al inventario.
     * Resuelve el problema N+1: 1 query para productos + 1 query para todos sus stocks.
     */
    private List<ProductoResponseDTO> toResponseDTOList(List<EntidadProducto> productos) {
        if (productos.isEmpty()) return List.of();

        List<Integer> ids = productos.stream()
                .map(EntidadProducto::getIdProducto)
                .collect(Collectors.toList());

        // Una sola consulta SQL para todos los stocks
        Map<Integer, Integer> stockMap = inventarioRepository.findByProducto_IdProductoIn(ids)
                .stream()
                .collect(Collectors.toMap(
                        i -> i.getProducto().getIdProducto(),
                        EntidadInventario::getStockFisico
                ));

        return productos.stream().map(p -> {
            ProductoResponseDTO dto = mapper.map(p, ProductoResponseDTO.class);
            dto.setStockFisico(stockMap.getOrDefault(p.getIdProducto(), 0));
            return dto;
        }).collect(Collectors.toList());
    }
}
