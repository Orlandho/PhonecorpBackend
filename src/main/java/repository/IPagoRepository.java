package repository;

import domain.EntidadPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la Entidad Pago.
 * Responsabilidad: Registrar las transacciones financieras asociadas a una orden.
 */
@Repository
public interface IPagoRepository extends JpaRepository<EntidadPago, Integer> {
}