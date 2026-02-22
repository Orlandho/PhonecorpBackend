package repository;

import domain.EntidadComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la Entidad Comprobante.
 * Responsabilidad: Guardar facturas y boletas emitidas tras la validacion con SUNAT.
 */
@Repository
public interface IComprobanteRepository extends JpaRepository<EntidadComprobante, Integer> {
}