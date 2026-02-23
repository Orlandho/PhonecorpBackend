package controller;

import domain.EntidadComprobante;
import repository.IComprobanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comprobantes")
public class ComprobanteController {

    private final IComprobanteRepository comprobanteRepository;

    @Autowired
    public ComprobanteController(IComprobanteRepository comprobanteRepository) {
        this.comprobanteRepository = comprobanteRepository;
    }

    @GetMapping
    public List<EntidadComprobante> listar() {
        return comprobanteRepository.findAll();
    }

    @GetMapping("/{idComprobante}")
    public EntidadComprobante obtenerPorId(@PathVariable Integer idComprobante) {
        return comprobanteRepository.findById(idComprobante)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Comprobante no encontrado con ID: " + idComprobante
                ));
    }

    @GetMapping("/pago/{idPago}")
    public EntidadComprobante obtenerPorPago(@PathVariable Integer idPago) {
        return comprobanteRepository.buscarPorIdPago(idPago)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe comprobante para el pago ID: " + idPago
                ));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntidadComprobante crear(@RequestBody EntidadComprobante c) {

        if (c.getIdPago() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idPago es obligatorio.");
        if (c.getTipoComprobante() == null || c.getTipoComprobante().trim().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tipoComprobante es obligatorio.");
        if (c.getSerie() == null || c.getSerie().trim().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "serie es obligatorio.");
        if (c.getNumeroCorrelativo() == null || c.getNumeroCorrelativo().trim().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "numeroCorrelativo es obligatorio.");

        // 1:1 con pago
        if (comprobanteRepository.existePorIdPago(c.getIdPago())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un comprobante para el pago ID: " + c.getIdPago()
            );
        }

        // Evitar duplicar correlativo
        if (comprobanteRepository.existePorCorrelativo(c.getTipoComprobante(), c.getSerie(), c.getNumeroCorrelativo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe el comprobante: " + c.getTipoComprobante() + " " + c.getSerie() + "-" + c.getNumeroCorrelativo()
            );
        }

        if (c.getFechaEmision() == null) c.setFechaEmision(LocalDateTime.now());

        return comprobanteRepository.save(c);
    }
}