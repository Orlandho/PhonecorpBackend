package com.phonecorp.phonecorpbackend.controller;

import com.phonecorp.phonecorpbackend.dto.ClienteRequestDTO;
import com.phonecorp.phonecorpbackend.dto.ClienteResponseDTO;
import com.phonecorp.phonecorpbackend.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BCE - Frontera: ClienteController
 * Recibe peticiones HTTP y delega toda la logica al ClienteService (Gestor).
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return clienteService.listarTodos();
    }

    @GetMapping("/{idCliente}")
    public ClienteResponseDTO obtenerPorId(@PathVariable Integer idCliente) {
        return clienteService.obtenerPorId(idCliente);
    }

    @GetMapping("/buscar")
    public ClienteResponseDTO buscarPorDniCe(@RequestParam("dni") String dniCe) {
        return clienteService.buscarPorDni(dniCe);
    }

    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDTO registrar(@RequestBody ClienteRequestDTO request) {
        return clienteService.registrar(request);
    }

    @PutMapping("/{idCliente}")
    public ClienteResponseDTO actualizar(@PathVariable Integer idCliente,
                                          @RequestBody ClienteRequestDTO request) {
        return clienteService.actualizar(idCliente, request);
    }
}