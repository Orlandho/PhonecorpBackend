package repository;

import domain.EntidadComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio: IComprobanteRepository
 * Responsabilidad: Acceso a datos de Comprobante.
 *
 * SOLID - SRP:
 * Se limita a consultas/persistencia (sin lógica de negocio).
 */
@Repository
public interface IComprobanteRepository extends JpaRepository<EntidadComprobante, Integer> {

    /**
     * Método: existePorIdPago
     * Propósito: Validar UNIQUE(id_pago) en Comprobante.
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM EntidadComprobante c WHERE c.idPago = :idPago")
    boolean existePorIdPago(@Param("idPago") Integer idPago);

    /**
     * Método: buscarPorIdPago
     * Propósito: Obtener comprobante por id_pago (1 a 1).
     */
    @Query("SELECT c FROM EntidadComprobante c WHERE c.idPago = :idPago")
    Optional<EntidadComprobante> buscarPorIdPago(@Param("idPago") Integer idPago);
}