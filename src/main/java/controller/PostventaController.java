package com.phonecorp.phonecorpbackend.controller;

import com.phonecorp.phonecorpbackend.dto.TicketPostventaRequestDTO;
import com.phonecorp.phonecorpbackend.dto.TicketPostventaResponseDTO;
import com.phonecorp.phonecorpbackend.service.PostventaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * BCE - Frontera: PostventaController
 * Recibe peticiones HTTP y delega toda la logica al PostventaService (Gestor).
 */
@RestController
@RequestMapping("/api/postventa")
public class PostventaController {

    private final PostventaService postventaService;

    @Autowired
    public PostventaController(PostventaService postventaService) {
        this.postventaService = postventaService;
    }

    @PostMapping("/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketPostventaResponseDTO registrarTicket(@RequestBody TicketPostventaRequestDTO request) {
        return postventaService.registrarTicket(request);
    }

    @GetMapping("/tickets/{idTicket}")
    public TicketPostventaResponseDTO obtenerTicket(@PathVariable Integer idTicket) {
        return postventaService.obtenerTicket(idTicket);
    }

    @GetMapping("/pedido/{idOrden}/estado")
    public Map<String, Object> consultarEstadoPedido(@PathVariable Integer idOrden) {
        return postventaService.consultarEstadoPedido(idOrden);
    }
}
