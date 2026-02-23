package controller;

import domain.EntidadCliente;
import repository.IClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final IClienteRepository clienteRepository;

    @Autowired
    public ClienteController(IClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public List<EntidadCliente> listar() {
        return clienteRepository.findAll();
    }

    @GetMapping("/{idCliente}")
    public EntidadCliente obtenerPorId(@PathVariable Integer idCliente) {
        return clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + idCliente
                ));
    }

    @GetMapping("/buscar")
    public EntidadCliente buscarPorDniCe(@RequestParam("dni") String dniCe) {
        return clienteRepository.buscarPorDniCe(dniCe)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado con DNI/CE: " + dniCe
                ));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntidadCliente crear(@RequestBody EntidadCliente cliente) {

        if (cliente.getDniCe() == null || cliente.getDniCe().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dniCe es obligatorio.");
        }
        if (cliente.getNombresCompletos() == null || cliente.getNombresCompletos().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombresCompletos es obligatorio.");
        }
        if (cliente.getDireccion() == null || cliente.getDireccion().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "direccion es obligatorio.");
        }

        if (clienteRepository.existePorDniCe(cliente.getDniCe())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un cliente con DNI/CE: " + cliente.getDniCe()
            );
        }

        if (cliente.getFechaRegistro() == null) {
            cliente.setFechaRegistro(LocalDateTime.now());
        }

        return clienteRepository.save(cliente);
    }
}