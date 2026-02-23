package controller;

import domain.EntidadCliente;
import dto.ClienteRequestDTO;
import dto.ClienteResponseDTO;
import exception.DniInvalidoException;
import repository.IClienteRepository;
import service.ReniecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BCE - Control: GestorClientes
 * Orquesta registro y actualización de clientes, validando identidad con RENIEC.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final IClienteRepository clienteRepository;
    private final ReniecService reniecService;

    @Autowired
    public ClienteController(IClienteRepository clienteRepository, ReniecService reniecService) {
        this.clienteRepository = clienteRepository;
        this.reniecService = reniecService;
    }

    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return clienteRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idCliente}")
    public ClienteResponseDTO obtenerPorId(@PathVariable Integer idCliente) {
        return toResponseDTO(clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + idCliente)));
    }

    @GetMapping("/buscar")
    public ClienteResponseDTO buscarPorDniCe(@RequestParam("dni") String dniCe) {
        return toResponseDTO(clienteRepository.buscarPorDniCe(dniCe)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado con DNI/CE: " + dniCe)));
    }

    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDTO registrar(@RequestBody ClienteRequestDTO request) {

        if (request.getDniCe() == null || request.getDniCe().isBlank())
            throw new IllegalArgumentException("El campo dniCe es obligatorio.");
        if (request.getNombresCompletos() == null || request.getNombresCompletos().isBlank())
            throw new IllegalArgumentException("El campo nombresCompletos es obligatorio.");
        if (request.getDireccion() == null || request.getDireccion().isBlank())
            throw new IllegalArgumentException("El campo direccion es obligatorio.");

        // Validación de identidad con RENIEC (mock)
        if (!reniecService.validarIdentidad(request.getDniCe()))
            throw new DniInvalidoException("DNI/CE inválido según RENIEC: " + request.getDniCe());

        if (clienteRepository.existePorDniCe(request.getDniCe()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un cliente con DNI/CE: " + request.getDniCe());

        EntidadCliente nuevo = new EntidadCliente();
        nuevo.setDniCe(request.getDniCe());
        nuevo.setNombresCompletos(request.getNombresCompletos());
        nuevo.setDireccion(request.getDireccion());
        nuevo.setTelefono(request.getTelefono());
        nuevo.setEmail(request.getEmail());
        nuevo.setHistorialCrediticio(
                request.getHistorialCrediticio() != null ? request.getHistorialCrediticio() : "BUENO");
        nuevo.setFechaRegistro(LocalDateTime.now());

        return toResponseDTO(clienteRepository.save(nuevo));
    }

    @PutMapping("/{idCliente}")
    public ClienteResponseDTO actualizar(@PathVariable Integer idCliente,
                                          @RequestBody ClienteRequestDTO request) {
        EntidadCliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + idCliente));

        if (request.getNombresCompletos() != null && !request.getNombresCompletos().isBlank())
            cliente.setNombresCompletos(request.getNombresCompletos());
        if (request.getDireccion() != null && !request.getDireccion().isBlank())
            cliente.setDireccion(request.getDireccion());
        if (request.getTelefono() != null) cliente.setTelefono(request.getTelefono());
        if (request.getEmail() != null) cliente.setEmail(request.getEmail());
        if (request.getHistorialCrediticio() != null)
            cliente.setHistorialCrediticio(request.getHistorialCrediticio());

        return toResponseDTO(clienteRepository.save(cliente));
    }

    private ClienteResponseDTO toResponseDTO(EntidadCliente c) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setIdCliente(c.getIdCliente());
        dto.setDniCe(c.getDniCe());
        dto.setNombresCompletos(c.getNombresCompletos());
        dto.setDireccion(c.getDireccion());
        dto.setTelefono(c.getTelefono());
        dto.setEmail(c.getEmail());
        dto.setHistorialCrediticio(c.getHistorialCrediticio());
        dto.setFechaRegistro(c.getFechaRegistro());
        return dto;
    }
}