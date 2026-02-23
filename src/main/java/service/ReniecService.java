package com.phonecorp.phonecorpbackend.service;

import org.springframework.stereotype.Service;

/**
 * Simulación del servicio externo de RENIEC.
 * Mock: DNIs/CEs que terminan en '0' o '9' se consideran inválidos.
 * En producción, este método haría una llamada HTTP al API de RENIEC.
 */
@Service
public class ReniecService {

    public boolean validarIdentidad(String dniCe) {
        if (dniCe == null || dniCe.isBlank()) return false;
        char ultimo = dniCe.charAt(dniCe.length() - 1);
        return ultimo != '0' && ultimo != '9';
    }

    /** Simula la devolución del nombre completo registrado en RENIEC. */
    public String consultarNombre(String dniCe) {
        if (!validarIdentidad(dniCe)) return null;
        return "Persona Registrada (MOCK) - DNI " + dniCe;
    }
}
