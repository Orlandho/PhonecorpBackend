package com.phonecorp.phonecorpbackend.service;

import com.phonecorp.phonecorpbackend.domain.EntidadCliente;
import com.phonecorp.phonecorpbackend.dto.ClienteRequestDTO;
import com.phonecorp.phonecorpbackend.dto.ClienteResponseDTO;
import com.phonecorp.phonecorpbackend.exception.DniInvalidoException;
import com.phonecorp.phonecorpbackend.repository.IClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BCE - Control: GestorClientes
 * Orquesta el registro y actualizacion de clientes, validando identidad con RENIEC.
 */
@Service
public class ClienteService {

    private final IClienteRepository clienteRepository;
    private final ReniecService       reniecService;
    private final ModelMapper         mapper;

    @Autowired
    public ClienteService(IClienteRepository clienteRepository,
                           ReniecService reniecService,
                           ModelMapper mapper) {
        this.clienteRepository = clienteRepository;
        this.reniecService     = reniecService;
        this.mapper            = mapper;
    }

    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(c -> mapper.map(c, ClienteResponseDTO.class))
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO obtenerPorId(Integer idCliente) {
        return mapper.map(
                clienteRepository.findById(idCliente)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + idCliente)),
                ClienteResponseDTO.class);
    }

    public ClienteResponseDTO buscarPorDni(String dniCe) {
        return mapper.map(
                clienteRepository.buscarPorDniCe(dniCe)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Cliente no encontrado con DNI/CE: " + dniCe)),
                ClienteResponseDTO.class);
    }

    public ClienteResponseDTO registrar(ClienteRequestDTO request) {
        if (request.getDniCe() == null || request.getDniCe().isBlank())
            throw new IllegalArgumentException("El campo dniCe es obligatorio.");
        if (request.getNombresCompletos() == null || request.getNombresCompletos().isBlank())
            throw new IllegalArgumentException("El campo nombresCompletos es obligatorio.");
        if (request.getDireccion() == null || request.getDireccion().isBlank())
            throw new IllegalArgumentException("El campo direccion es obligatorio.");

        if (!reniecService.validarIdentidad(request.getDniCe()))
            throw new DniInvalidoException("DNI/CE invalido segun RENIEC: " + request.getDniCe());

        if (clienteRepository.existePorDniCe(request.getDniCe()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un cliente con DNI/CE: " + request.getDniCe());

        EntidadCliente nuevo = mapper.map(request, EntidadCliente.class);
        if (nuevo.getHistorialCrediticio() == null) nuevo.setHistorialCrediticio("BUENO");
        nuevo.setFechaRegistro(LocalDateTime.now());

        return mapper.map(clienteRepository.save(nuevo), ClienteResponseDTO.class);
    }

    public ClienteResponseDTO actualizar(Integer idCliente, ClienteRequestDTO request) {
        EntidadCliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + idCliente));

        if (request.getNombresCompletos() != null && !request.getNombresCompletos().isBlank())
            cliente.setNombresCompletos(request.getNombresCompletos());
        if (request.getDireccion() != null && !request.getDireccion().isBlank())
            cliente.setDireccion(request.getDireccion());
        if (request.getTelefono() != null)  cliente.setTelefono(request.getTelefono());
        if (request.getEmail() != null)     cliente.setEmail(request.getEmail());
        if (request.getHistorialCrediticio() != null)
            cliente.setHistorialCrediticio(request.getHistorialCrediticio());

        return mapper.map(clienteRepository.save(cliente), ClienteResponseDTO.class);
    }
}
