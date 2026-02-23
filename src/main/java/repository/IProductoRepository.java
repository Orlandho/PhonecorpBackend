package com.phonecorp.phonecorpbackend.repository;

import com.phonecorp.phonecorpbackend.domain.EntidadProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la Entidad Producto.
 * Responsabilidad: Acceso a datos del catalogo de equipos celulares.
 */
@Repository
public interface IProductoRepository extends JpaRepository<EntidadProducto, Integer> {
}