package repository;

import domain.EntidadTicketPostventa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la Entidad TicketPostventa.
 * Responsabilidad: Almacenar los reclamos y consultas registrados en el modulo de postventa.
 */
@Repository
public interface ITicketPostventaRepository extends JpaRepository<EntidadTicketPostventa, Integer> {
}