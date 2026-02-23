package repository;

import domain.EntidadDetalleOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * BCE - Repositorio para DetalleOrden.
 * Permite consultar los ítems de una orden de venta específica.
 */
@Repository
public interface IDetalleOrdenRepository extends JpaRepository<EntidadDetalleOrden, Integer> {

    List<EntidadDetalleOrden> findByOrden_IdOrden(Integer idOrden);
}
