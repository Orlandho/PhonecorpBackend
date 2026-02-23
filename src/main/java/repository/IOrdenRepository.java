package com.phonecorp.phonecorpbackend.repository;

import com.phonecorp.phonecorpbackend.domain.EntidadOrdenVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la Entidad OrdenVenta.
 * Responsabilidad: Persistencia de las ordenes comerciales generadas por el cliente.
 */
@Repository
public interface IOrdenRepository extends JpaRepository<EntidadOrdenVenta, Integer> {
}