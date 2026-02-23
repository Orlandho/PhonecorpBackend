package com.phonecorp.phonecorpbackend.service;

import com.phonecorp.phonecorpbackend.domain.*;
import com.phonecorp.phonecorpbackend.dto.*;
import com.phonecorp.phonecorpbackend.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * BCE - Control: GestorPostventa
 * Registra tickets de reclamo/consulta y permite consultar el estado de un pedido.
 */
@Service
public class PostventaService {

    private final ITicketPostventaRepository ticketRepository;
    private final IClienteRepository         clienteRepository;
    private final IOrdenRepository           ordenRepository;
    private final ModelMapper                mapper;

    @Autowired
    public PostventaService(ITicketPostventaRepository ticketRepository,
                             IClienteRepository clienteRepository,
                             IOrdenRepository ordenRepository,
                             ModelMapper mapper) {
        this.ticketRepository  = ticketRepository;
        this.clienteRepository = clienteRepository;
        this.ordenRepository   = ordenRepository;
        this.mapper            = mapper;
    }

    public TicketPostventaResponseDTO registrarTicket(TicketPostventaRequestDTO request) {

        EntidadCliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + request.getIdCliente()));

        if (request.getMotivo() == null || request.getMotivo().isBlank())
            throw new IllegalArgumentException("El motivo del ticket es obligatorio.");

        EntidadTicketPostventa ticket = new EntidadTicketPostventa();
        ticket.setCliente(cliente);
        ticket.setMotivo(request.getMotivo());
        ticket.setDescripcionCaso(request.getDescripcionCaso());
        ticket.setEstadoTicket("ABIERTO");

        if (request.getIdOrden() != null) {
            EntidadOrdenVenta orden = ordenRepository.findById(request.getIdOrden())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Orden no encontrada con ID: " + request.getIdOrden()));
            ticket.setOrden(orden);
        }

        return toResponseDTO(ticketRepository.save(ticket));
    }

    public TicketPostventaResponseDTO obtenerTicket(Integer idTicket) {
        return toResponseDTO(ticketRepository.findById(idTicket)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ticket no encontrado con ID: " + idTicket)));
    }

    public Map<String, Object> consultarEstadoPedido(Integer idOrden) {
        EntidadOrdenVenta orden = ordenRepository.findById(idOrden)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Orden no encontrada con ID: " + idOrden));
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("idOrden",          idOrden);
        respuesta.put("estado",           orden.getEstado());
        respuesta.put("modalidadEntrega", orden.getModalidadEntrega());
        respuesta.put("fechaEmision",     orden.getFechaEmision());
        respuesta.put("montoTotal",       orden.getMontoTotal());
        return respuesta;
    }

    private TicketPostventaResponseDTO toResponseDTO(EntidadTicketPostventa t) {
        TicketPostventaResponseDTO dto = new TicketPostventaResponseDTO();
        dto.setIdTicket(t.getIdTicket());
        dto.setIdCliente(t.getCliente().getIdCliente());
        dto.setNombreCliente(t.getCliente().getNombresCompletos());
        dto.setIdOrden(t.getOrden() != null ? t.getOrden().getIdOrden() : null);
        dto.setMotivo(t.getMotivo());
        dto.setDescripcionCaso(t.getDescripcionCaso());
        dto.setEstadoTicket(t.getEstadoTicket());
        dto.setFechaRegistro(t.getFechaRegistro());
        return dto;
    }
}
