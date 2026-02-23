package controller;

import domain.*;
import repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * BCE - Control: GestorInventarioDespacho
 * Actualiza el stock físico tras una venta exitosa y genera documentación de salida.
 */
@RestController
@RequestMapping("/api/inventario")
public class InventarioDespachoController {

    private final IInventarioRepository   inventarioRepository;
    private final IOrdenRepository        ordenRepository;
    private final IDetalleOrdenRepository detalleOrdenRepository;

    @Autowired
    public InventarioDespachoController(IInventarioRepository inventarioRepository,
                                         IOrdenRepository ordenRepository,
                                         IDetalleOrdenRepository detalleOrdenRepository) {
        this.inventarioRepository   = inventarioRepository;
        this.ordenRepository        = ordenRepository;
        this.detalleOrdenRepository = detalleOrdenRepository;
    }

    /**
     * Despacha una orden: reduce el stockFisico de cada producto
     * y genera el documento de salida correspondiente.
     */
    @PostMapping("/despachar/{idOrden}")
    public Map<String, Object> despacharOrden(@PathVariable Integer idOrden) {

        EntidadOrdenVenta orden = ordenRepository.findById(idOrden)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Orden no encontrada con ID: " + idOrden));

        if (!"PAGADO".equalsIgnoreCase(orden.getEstado()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Solo se puede despachar una orden en estado PAGADO. Estado actual: " + orden.getEstado());

        List<EntidadDetalleOrden> detalles = detalleOrdenRepository.findByOrden_IdOrden(idOrden);

        for (EntidadDetalleOrden detalle : detalles) {
            Integer idProducto = detalle.getProducto().getIdProducto();
            EntidadInventario inventario = inventarioRepository.findByProducto_IdProducto(idProducto)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Inventario no encontrado para producto ID: " + idProducto));

            int nuevoStock = inventario.getStockFisico() - detalle.getCantidad();
            if (nuevoStock < 0)
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Stock insuficiente al despachar producto ID: " + idProducto);

            inventario.setStockFisico(nuevoStock);
            inventario.setFechaActualizacion(LocalDateTime.now());
            inventarioRepository.save(inventario);
        }

        orden.setEstado("DESPACHADO");
        ordenRepository.save(orden);

        // Generar documento de salida simulado según modalidad
        String tipoDoc    = "Envío a Domicilio".equalsIgnoreCase(orden.getModalidadEntrega())
                ? "GUIA_REMISION" : "ACTA_ENTREGA";
        String numeroDoc  = "DOC-" + idOrden + "-" + (System.currentTimeMillis() % 100_000);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("mensaje",              "Despacho realizado correctamente.");
        respuesta.put("idOrden",              idOrden);
        respuesta.put("tipoDocumento",        tipoDoc);
        respuesta.put("numeroDocumento",      numeroDoc);
        respuesta.put("fechaDespacho",        LocalDateTime.now().toString());
        respuesta.put("productosActualizados", detalles.size());
        return respuesta;
    }

    @GetMapping("/stock/{idProducto}")
    public Map<String, Object> consultarStock(@PathVariable Integer idProducto) {
        int stock = inventarioRepository.consultarStock(idProducto).orElse(0);
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("idProducto", idProducto);
        respuesta.put("stockFisico", stock);
        respuesta.put("disponible",  stock > 0);
        return respuesta;
    }
}
